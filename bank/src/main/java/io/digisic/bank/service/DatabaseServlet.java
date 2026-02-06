package io.digisic.bank.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/queryDatabase")
public class DatabaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String dbType;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    // ----------------------------------------------------
    // Init
    // ----------------------------------------------------
    @Override
    public void init() {
        dbType = System.getenv("DB_TYPE");

        if ("h2".equalsIgnoreCase(dbType)) {
            // MUST match Spring Boot datasource exactly
            jdbcUrl = System.getenv("SPRING_DATASOURCE_URL");
            jdbcUser = System.getenv("SPRING_DATASOURCE_USERNAME");
            jdbcPassword = System.getenv("SPRING_DATASOURCE_PASSWORD");
        } else {
            if ("postgres".equalsIgnoreCase(dbType)) {
                dbType = "postgresql";
            }

            jdbcUrl =
                "jdbc:" + dbType + "://" +
                System.getenv("DB_HOST") + ":" +
                System.getenv("DB_PORT") + "/" +
                System.getenv("DB_NAME");

            jdbcUser = System.getenv("DB_USER");
            jdbcPassword = System.getenv("DB_PASSWORD");
        }
    }

    // ----------------------------------------------------
    // GET
    // ----------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            loadJdbcDriver(dbType);

            try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement stmt = conn.createStatement()) {

                out.println("<div class='ajax-table-container'>");
                out.println("<h2>Most Recent 20 Registered Users</h2>");

                renderUsersTable(out, stmt);
                renderDatabaseInfo(out, stmt);

                out.println("</div>");

            } catch (SQLException e) {
                out.println("<h3 style='color:red;'>Database error: " + e.getMessage() + "</h3>");
            }

        } catch (Exception e) {
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }

    // ----------------------------------------------------
    // Users table
    // ----------------------------------------------------
    private void renderUsersTable(PrintWriter out, Statement stmt) throws SQLException {

        String userQuery =
            "SELECT u.id, p.title, p.first_name, p.last_name, u.username, " +
            "CAST(p.dob AS DATE) AS dob, p.ssn, p.address, p.postal_code, p.region, p.mobile_phone " +
            "FROM users u " +
            "LEFT JOIN user_profile p ON u.profile_id = p.id " +
            "WHERE u.username <> 'admin@demo.io' " +
            "ORDER BY u.id DESC " +
            "LIMIT 20";

        out.println("<div style='overflow-x:auto;'>");
        out.println("<table class='userTable'>");
        out.println("<thead><tr>");
        out.println("<th>ID</th><th>Title</th><th>First</th><th>Last</th><th>Email</th>");
        out.println("<th>DOB</th><th>SSN</th><th>Address</th><th>Postcode</th><th>Region</th><th>Mobile</th>");
        out.println("</tr></thead><tbody>");

        try (ResultSet rs = stmt.executeQuery(userQuery)) {
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + safe(rs.getString("title")) + "</td>");
                out.println("<td>" + safe(rs.getString("first_name")) + "</td>");
                out.println("<td>" + safe(rs.getString("last_name")) + "</td>");
                out.println("<td>" + safe(rs.getString("username")) + "</td>");
                out.println("<td>" + safe(rs.getString("dob")) + "</td>");
                out.println("<td>" + safe(rs.getString("ssn")) + "</td>");
                out.println("<td>" + safe(rs.getString("address")) + "</td>");
                out.println("<td>" + safe(rs.getString("postal_code")) + "</td>");
                out.println("<td>" + safe(rs.getString("region")) + "</td>");
                out.println("<td>" + safe(rs.getString("mobile_phone")) + "</td>");
                out.println("</tr>");
            }
        }

        out.println("</tbody></table></div>");
    }

    // ----------------------------------------------------
    // Database info
    // ----------------------------------------------------
    private void renderDatabaseInfo(PrintWriter out, Statement stmt) throws SQLException {

        String lastUpdateQuery =
            "SELECT MAX(transaction_date) AS last_update FROM account_transaction";

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

    }

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------
    private void loadJdbcDriver(String dbType) throws ClassNotFoundException {
        switch (dbType.toLowerCase()) {
            case "mysql":
                Class.forName("com.mysql.cj.jdbc.Driver");
                break;
            case "postgresql":
                Class.forName("org.postgresql.Driver");
                break;
            case "h2":
                Class.forName("org.h2.Driver");
                break;
            default:
                throw new ClassNotFoundException("Unsupported DB_TYPE: " + dbType);
        }
    }

    private String safe(String value) {
        return value != null ? value : "N/A";
    }
}