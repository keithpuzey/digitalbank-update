package io.digisic.bank.service;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/userReport")
public class UserReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String dbType, dbHost, dbPort, dbUser, dbPassword;
    private String bankUrl, creditUrl;

    @Override
    public void init() {
    dbType = System.getenv("DB_TYPE");
    dbUser = System.getenv("DB_USER");
    dbPassword = System.getenv("DB_PASSWORD");

    if ("h2".equalsIgnoreCase(dbType)) {
        // MUST match Spring datasource exactly
        bankUrl = "jdbc:h2:mem:digitalbank;MODE=MySQL;DB_CLOSE_DELAY=-1";

        // Optional: reuse same DB for credit in H2
        creditUrl = bankUrl;
    } else {
        dbHost = System.getenv("DB_HOST");
        dbPort = System.getenv("DB_PORT");

        bankUrl =
            "jdbc:" + normalizeDbType(dbType) + "://" +
            dbHost + ":" + dbPort + "/digitalbank";

        creditUrl =
            "jdbc:" + normalizeDbType(dbType) + "://" +
            dbHost + ":" + dbPort + "/digitalcredit";
    }
}

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;charset=UTF-8");
    String type = request.getParameter("type");
    if (type == null) type = "registered";

    try (PrintWriter out = response.getWriter()) {
        loadJDBCDriver(dbType);

        Map<String, String[]> bankUsers;
        Map<String, String[]> creditUsers = null;

        // Load DigitalBank users
        try {
            bankUsers = loadUsers(bankUrl);
        } catch (SQLException e) {
            out.println("<h3 style='color:red;'>DigitalBank database is not available: " + e.getMessage() + "</h3>");
            return;
        }

        // Load DigitalCredit users only for common/inconsistent reports
        if ("common".equals(type) || "inconsistent".equals(type)) {
            try {
                creditUsers = loadUsers(creditUrl);
            } catch (SQLException e) {
                out.println("<h3 style='color:red;'>DigitalCredit database is not available: " + e.getMessage() + "</h3>");
                return;
            }
        }

        out.println("<div class='ajax-table-container'>");

        switch (type) {
            case "registered":
                renderRegisteredUsers(out, bankUsers);
                break;
            case "common":
                renderCommonUsers(out, bankUsers, creditUsers);
                break;
            case "inconsistent":
                renderInconsistentUsers(out, bankUsers, creditUsers);
                break;
            default:
                out.println("<p>Unknown report type.</p>");
        }

        // --- Add Database Type and Last Update ---
        try (Connection conn = DriverManager.getConnection(bankUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            // Get last transaction date
            String lastUpdateQuery = "SELECT MAX(transaction_date) AS last_update FROM account_transaction";
            String lastUpdate = null;
            try (ResultSet rs = stmt.executeQuery(lastUpdateQuery)) {
                if (rs.next()) {
                    lastUpdate = rs.getString("last_update");
                }
            }

            out.println("<hr>");
            out.println("<h3>Database Info</h3>");
            out.println("<p>Database Type: <strong>" + dbType + "</strong></p>");
            out.println("<p>Last Database Update: <strong>" + 
                        (lastUpdate != null ? lastUpdate : "No data available") + 
                        "</strong></p>");
                        // Add user count
            String userCount = "0";
            String countQuery = "SELECT COUNT(*) AS total_users FROM users";
            try (ResultSet rsCount = stmt.executeQuery(countQuery)) {
                if (rsCount.next()) {
                    int total = rsCount.getInt("total_users"); // get as int
                    total = total - 1; // subtract 1
                    userCount = Integer.toString(total); // convert back to string
                }
}
            out.println("<p>Total Users: <strong>" + userCount + "</strong></p>");

        } catch (SQLException e) {
            out.println("<p style='color:red;'>Could not fetch last update: " + e.getMessage() + "</p>");
        }

        out.println("</div>"); // ajax-table-container

    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
    }
}

private Map<String, String[]> loadUsers(String url) throws SQLException {
    Map<String, String[]> users = new LinkedHashMap<>(); // preserves insertion order
    String query = "SELECT u.id, p.title, p.first_name, p.last_name, u.username, " +
                   "CAST(p.dob AS DATE) AS dob, p.ssn, p.address, p.postal_code, p.region, p.mobile_phone " +
                   "FROM users u " +
                   "LEFT JOIN user_profile p ON u.profile_id = p.id " +
                   "WHERE u.username <> 'admin@demo.io' " +
                   "ORDER BY u.id DESC " +
                   "LIMIT 20";

    try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            users.put(rs.getString("username"),
                      new String[]{
                          rs.getString("first_name"),
                          rs.getString("last_name"),
                          rs.getString("ssn"),
                          rs.getString("dob"),
                          rs.getString("region"),
                          rs.getString("postal_code")
                      });
        }
    }
    return users;
}

    private void renderRegisteredUsers(PrintWriter out, Map<String, String[]> bankUsers) {
        out.println("<h2>Registered Users (DigitalBank)</h2>");
        out.println("<div style='overflow-x:auto;'><table class='userTable'>");
        out.println("<thead><tr><th>Email</th><th>First Name</th><th>Last Name</th><th>SSN</th><th>DOB</th><th>Region</th><th>Postcode</th></tr></thead><tbody>");
        for (Map.Entry<String, String[]> e : bankUsers.entrySet()) {
            String email = e.getKey();
            String[] u = e.getValue();
            out.println("<tr><td>" + email + "</td><td>" + u[0] + "</td><td>" + u[1] + "</td><td>" + u[2] + "</td><td>" + u[3] + "</td><td>" + u[4] + "</td><td>" + u[5] + "</td></tr>");
        }
        out.println("</tbody></table></div>");
    }

    private void renderCommonUsers(PrintWriter out, Map<String, String[]> bankUsers, Map<String, String[]> creditUsers) {
        out.println("<h2>Users in Both Databases</h2>");
        out.println("<div style='overflow-x:auto;'><table class='userTable'>");
        out.println("<thead><tr><th>Email</th><th>First Name</th><th>Last Name</th><th>SSN</th><th>DOB</th><th>Region</th><th>Postcode</th></tr></thead><tbody>");
        for (String email : bankUsers.keySet()) {
            if (creditUsers.containsKey(email)) {
                String[] u = bankUsers.get(email);
                out.println("<tr><td>" + email + "</td><td>" + u[0] + "</td><td>" + u[1] + "</td><td>" + u[2] + "</td><td>" + u[3] + "</td><td>" + u[4] + "</td><td>" + u[5] + "</td></tr>");
            }
        }
        out.println("</tbody></table></div>");
    }

    private void renderInconsistentUsers(PrintWriter out, Map<String, String[]> bankUsers, Map<String, String[]> creditUsers) {
        out.println("<h2>Inconsistent Users</h2>");
        out.println("<div style='overflow-x:auto;'><table class='userTable'>");
        out.println("<thead><tr><th>Email</th><th>Bank DB</th><th>Credit DB</th></tr></thead><tbody>");
        for (String email : bankUsers.keySet()) {
            if (creditUsers.containsKey(email)) {
                String[] b = bankUsers.get(email);
                String[] c = creditUsers.get(email);
                if (!Arrays.equals(b, c)) {
                    out.println("<tr><td>" + email + "</td><td>" + Arrays.toString(b) + "</td><td>" + Arrays.toString(c) + "</td></tr>");
                }
            }
        }
        out.println("</tbody></table></div>");
    }

    private void loadJDBCDriver(String dbType) throws ClassNotFoundException {
        switch (dbType.toLowerCase()) {
            case "mysql": Class.forName("com.mysql.cj.jdbc.Driver"); break;
            case "postgres": case "postgresql": Class.forName("org.postgresql.Driver"); break;
            case "h2": Class.forName("org.h2.Driver"); break;
            default: throw new ClassNotFoundException("Unsupported DB_TYPE: " + dbType);
        }
    }

    private String normalizeDbType(String dbType) {
        return dbType.equalsIgnoreCase("postgres") ? "postgresql" : dbType.toLowerCase();
    }
}