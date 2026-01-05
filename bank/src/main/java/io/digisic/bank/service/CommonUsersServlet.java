package io.digisic.bank.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/commonUsers")
public class CommonUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String dbType, dbHost, dbPort, dbUser, dbPassword;

    @Override
    public void init() {
        dbType = System.getenv("DB_TYPE");
        dbHost = System.getenv("DB_HOST");
        dbPort = System.getenv("DB_PORT");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            loadJDBCDriver(dbType);

            String bankUrl   = "jdbc:" + normalizeDbType(dbType) + "://" + dbHost + ":" + dbPort + "/digitalbank";
            String creditUrl = "jdbc:" + normalizeDbType(dbType) + "://" + dbHost + ":" + dbPort + "/digitalcredit";

            // Load DigitalBank users
            Map<String, String[]> bankUsers = new HashMap<>();
            try (Connection conn = DriverManager.getConnection(bankUrl, dbUser, dbPassword);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT ssn, first_name, last_name, email FROM user_profile")) {
                while (rs.next()) {
                    bankUsers.put(rs.getString("ssn"),
                            new String[]{rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")});
                }
            }

            // Compare with DigitalCredit users
            out.println("<h2>Users in Both Databases</h2>");
            out.println("<table border='1'><tr><th>SSN</th><th>First Name</th><th>Last Name</th><th>Email</th></tr>");

            try (Connection conn = DriverManager.getConnection(creditUrl, dbUser, dbPassword);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT ssn, first_name, last_name, email FROM user_profile")) {
                while (rs.next()) {
                    String ssn = rs.getString("ssn");
                    if (bankUsers.containsKey(ssn)) {
                        String[] user = bankUsers.get(ssn);
                        out.println("<tr><td>" + ssn + "</td><td>" + user[0] + "</td><td>" + user[1] + "</td><td>" + user[2] + "</td></tr>");
                    }
                }
            }

            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
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

    private String normalizeDbType(String dbType) {
        return dbType.equalsIgnoreCase("postgres") ? "postgresql" : dbType.toLowerCase();
    }
}