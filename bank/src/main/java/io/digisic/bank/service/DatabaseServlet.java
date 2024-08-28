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
    private String jdbcUrl = "jdbc:"+ System.getenv("DB_TYPE") + "://" + System.getenv("DB_HOST") + ":" + System.getenv("DB_PORT") + "/" + System.getenv("DB_NAME");
    private String jdbcUser = System.getenv("DB_USER");
    private String jdbcPassword = System.getenv("DB_PASSWORD");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query to get the last 10 registered users
            String sql = "SELECT u.id,  p.first_name, p.last_name " +
                         "FROM users u " +
                         "LEFT JOIN user_profile p ON u.profile_id = p.id " +
                         "ORDER BY u.id DESC " +
                         "LIMIT 10";
            ResultSet resultSet = statement.executeQuery(sql);

            // Process the result set for the most recent 10 users
            out.println("<html><body><h2>Most Recent 10 Registered Users</h2><table border='1'>");
            out.println("<tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
            while (resultSet.next()) {
                out.println("<tr><td>" + resultSet.getInt("id") + "</td>" + "<td>" + 
                            (resultSet.getString("first_name") != null ? resultSet.getString("first_name") : "N/A") + "</td><td>" + 
                            (resultSet.getString("last_name") != null ? resultSet.getString("last_name") : "N/A") + "</td></tr>");
            }
            out.println("</table>");

            // Execute the query to get the last opened account
            String accountQuery = "SELECT MAX(transaction_date) AS last_opened_account FROM account_transaction";
            ResultSet accountResultSet = statement.executeQuery(accountQuery);

            // Process the result for the last opened account
            if (accountResultSet.next()) {
                String lastOpenedAccount = accountResultSet.getString("last_opened_account");
                if (lastOpenedAccount != null) {
                    out.println("<h2>Last DataBase Update - Date/Time: " + lastOpenedAccount + "</h2>");
                } else {
                    out.println("<h2>Last DataBase Update - Date/Time: No data available</h2>");
                }
            }

            // Close the connections
            accountResultSet.close();
            resultSet.close();
            statement.close();
            connection.close();

            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<html><body><h2>SQL Error: " + e.getMessage() + "</h2></body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body><h2>Error: " + e.getMessage() + "</h2></body></html>");
        }
    }
}