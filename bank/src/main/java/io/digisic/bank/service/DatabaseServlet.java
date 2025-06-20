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
            loadJDBCDriver(System.getenv("DB_TYPE"));

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) {

                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("<title>Registered Users Query</title>");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }");
                out.println("h2 { color: #333; }");
                out.println("table { border-collapse: collapse; width: 100%; background: #fff; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
                out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
                out.println("th { background-color: #4CAF50; color: white; cursor: pointer; }");
                out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                out.println(".button { margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; border: none; color: white; font-size: 14px; cursor: pointer; border-radius: 5px; }");
                out.println(".button:hover { background-color: #45a049; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");

                out.println("<h2>Most Recent 30 Registered Users</h2>");
                out.println("<table id='userTable'>");
                out.println("<thead><tr><th>ID</th><th>Title</th><th>First Name</th><th>Last Name</th><th>Email Address</th><th>DOB</th><th>SSN</th><th>Address</th><th>Zip Code</th><th>Region</th><th>Mobile Number</th></tr></thead>");
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

                // Last database update timestamp
                String accountQuery = "SELECT MAX(transaction_date) AS last_opened_account FROM account_transaction";
                try (ResultSet accountResultSet = statement.executeQuery(accountQuery)) {
                    if (accountResultSet.next()) {
                        String lastOpenedAccount = accountResultSet.getString("last_opened_account");
                        out.println("<h2>Last Database Update - Date/Time: <span style='color: #4CAF50;'>" +
                                (lastOpenedAccount != null ? lastOpenedAccount : "No data available") + "</span></h2>");
                    }
                }

  

                // Add JavaScript for sorting and export
                out.println("<script>");
                out.println("document.querySelectorAll('th').forEach((header, index) => {");
                out.println("  header.addEventListener('click', () => {");
                out.println("    const table = header.closest('table');");
                out.println("    const rows = Array.from(table.querySelectorAll('tbody > tr'));");
                out.println("    const ascending = header.classList.toggle('asc');");
                out.println("    header.classList.toggle('desc', !ascending);");
                out.println("    rows.sort((a, b) => {");
                out.println("      const cellA = a.children[index].innerText.toLowerCase();");
                out.println("      const cellB = b.children[index].innerText.toLowerCase();");
                out.println("      return ascending ? cellA.localeCompare(cellB) : cellB.localeCompare(cellA);");
                out.println("    });");
                out.println("    rows.forEach(row => table.querySelector('tbody').appendChild(row));");
                out.println("  });");
                out.println("});");


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

    private String safe(String value) {
        return value != null ? value : "N/A";
    }
}