package io.digisic.bank.service;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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
        dbHost = System.getenv("DB_HOST");
        dbPort = System.getenv("DB_PORT");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");

        bankUrl   = "jdbc:" + normalizeDbType(dbType) + "://" + dbHost + ":" + dbPort + "/digitalbank";
        creditUrl = "jdbc:" + normalizeDbType(dbType) + "://" + dbHost + ":" + dbPort + "/digitalcredit";
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

            out.println("</div>");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }

    private Map<String, String[]> loadUsers(String url) throws SQLException {
        Map<String, String[]> users = new HashMap<>();
        String query = "SELECT email_address, first_name, last_name, ssn, dob, region, postal_code " +
                "FROM user_profile " +
                "WHERE email_address NOT IN ('jsmith@demo.io', 'nsmith@demo.io', 'admin@demo.io')";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.put(rs.getString("email_address"),
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