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

    // Load environment variables
    private String jdbcUrl = "jdbc:" + System.getenv("DB_TYPE") + "://" + 
                             System.getenv("DB_HOST") + ":" + 
                             System.getenv("DB_PORT") + "/" + 
                             System.getenv("DB_NAME");
    private String jdbcUser = System.getenv("DB_USER");
    private String jdbcPassword = System.getenv("DB_PASSWORD");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            // Load the appropriate JDBC driver based on the DB_TYPE
            loadJDBCDriver(System.getenv("DB_TYPE"));

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) {

                // Query for the last 10 registered users
                String userQuery = "SELECT u.id, p.first_name, p.last_name " +
                                   "FROM users u " +
                                   "LEFT JOIN user_profile p ON u.profile_id = p.id " +
                                   "ORDER BY u.id DESC " +
                                   "LIMIT 10";
                try (ResultSet userResultSet = statement.executeQuery(userQuery)) {
                    // Render the results
                    out.println("<html><body><h2>Most Recent 10 Registered Users</h2><table border='1'>");
                    out.println("<tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
                    while (userResultSet.next()) {
                        out.println("<tr><td>" + userResultSet.getInt("id") + "</td><td>" +
                                    (userResultSet.getString("first_name") != null ? userResultSet.getString("first_name") : "N/A") + "</td><td>" +
                                    (userResultSet.getString("last_name") != null ? userResultSet.getString("last_name") : "N/A") + "</td></tr>");
                    }
                    out.println("</table>");
                }

                // Query for the last opened account
                String accountQuery = "SELECT MAX(transaction_date) AS last_opened_account FROM account_transaction";
                try (ResultSet accountResultSet = statement.executeQuery(accountQuery)) {
                    if (accountResultSet.next()) {
                        String lastOpenedAccount = accountResultSet.getString("last_opened_account");
                        out.println("<h2>Last Database Update - Date/Time: " +
                                    (lastOpenedAccount != null ? lastOpenedAccount : "No data available") + "</h2>");
                    }
                }
                out.println("</body></html>");

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("<html><body><h2>SQL Error: " + e.getMessage() + "</h2></body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("<html><body><h2>Error: " + e.getMessage() + "</h2></body></html>");
        }
    }

    private void loadJDBCDriver(String dbType) throws ClassNotFoundException {
        switch (dbType.toLowerCase()) {
            case "mysql":
                Class.forName("com.mysql.cj.jdbc.Driver");
                break;
            case "postgres":
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
}