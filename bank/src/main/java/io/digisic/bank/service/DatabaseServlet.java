package io.digisic.bank.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/queryDatabase")
public class DatabaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    @Override
    public void init() {
        String dbType = System.getenv("DB_TYPE");
        if ("postgres".equalsIgnoreCase(dbType)) {
            dbType = "postgresql"; // fix JDBC URL
        }
        jdbcUrl = "jdbc:" + dbType + "://" +
                  System.getenv("DB_HOST") + ":" +
                  System.getenv("DB_PORT") + "/" +
                  System.getenv("DB_NAME");
        jdbcUser = System.getenv("DB_USER");
        jdbcPassword = System.getenv("DB_PASSWORD");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            loadJDBCDriver(System.getenv("DB_TYPE"));

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) {

                // Output only the table and last update, no <html>/<body>
                out.println("<h2>Most Recent 30 Registered Users</h2>");
                out.println("<table id='userTable'>");
                out.println("<thead><tr><th>ID</th><th>Title</th><th>First Name</th><th>Last Name</th><th>Email</th><th>DOB</th><th>SSN</th><th>Address</th><th>Zip</th><th>Region</th><th>Mobile</th></tr></thead>");
                out.println("<tbody>");

                String userQuery = "SELECT u.id, p.title, p.first_name, p.last_name, u.username, DATE(p.dob) AS dob, p.ssn, p.address, p.postal_code, p.region, p.mobile_phone " +
                                   "FROM users u " +
                                   "LEFT JOIN user_profile p ON u.profile_id = p.id " +
                                   "ORDER BY u.id DESC " +
                                   "LIMIT 30";
                try (ResultSet rs = statement.executeQuery(userQuery)) {
                    while (rs.next()) {
                        out.println("<tr><td>" + rs.getInt("id") + "</td><td>" +
                                safe(rs.getString("title")) + "</td><td>" +
                                safe(rs.getString("first_name")) + "</td><td>" +
                                safe(rs.getString("last_name")) + "</td><td>" +
                                safe(rs.getString("username")) + "</td><td>" +
                                safe(rs.getString("dob")) + "</td><td>" +
                                safe(rs.getString("ssn")) + "</td><td>" +
                                safe(rs.getString("address")) + "</td><td>" +
                                safe(rs.getString("postal_code")) + "</td><td>" +
                                safe(rs.getString("region")) + "</td><td>" +
                                safe(rs.getString("mobile_phone")) + "</td></tr>");
                    }
                }
                out.println("</tbody></table>");

                // Last database update
                String accountQuery = "SELECT MAX(transaction_date) AS last_opened_account FROM account_transaction";
                try (ResultSet accountResultSet = statement.executeQuery(accountQuery)) {
                    if (accountResultSet.next()) {
                        String lastOpenedAccount = accountResultSet.getString("last_opened_account");
                        out.println("<h3>Last Database Update: <span style='color: #4CAF50;'>" +
                                (lastOpenedAccount != null ? lastOpenedAccount : "No data available") + "</span></h3>");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("<h3>SQL Error: " + e.getMessage() + "</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }

    private void loadJDBCDriver(String dbType) throws ClassNotFoundException {
        switch (dbType.toLowerCase()) {
            case "mysql": Class.forName("com.mysql.cj.jdbc.Driver"); break;
            case "postgres": case "postgresql": Class.forName("org.postgresql.Driver"); break;
            case "h2": Class.forName("org.h2.Driver"); break;
            default: throw new ClassNotFoundException("Unsupported DB_TYPE: " + dbType);
        }
    }

    private String safe(String value) {
        return value != null ? value : "N/A";
    }
}