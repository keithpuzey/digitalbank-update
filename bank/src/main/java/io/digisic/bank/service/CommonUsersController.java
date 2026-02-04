package io.digisic.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class CommonUsersController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/queryDatabase")
    public String getCommonUsers(Model model) {

        // Load users from "DigitalBank"
        Map<String, UserRow> bankUsers = new HashMap<>();

        jdbcTemplate.query(
            "SELECT ssn, first_name, last_name, email FROM user_profile",
            rs -> {
                bankUsers.put(
                    rs.getString("ssn"),
                    new UserRow(
                        rs.getString("ssn"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                    )
                );
            }
        );

        // For H2: same DB used for comparison
        // (In real DBs this could be another schema or datasource)
        List<UserRow> commonUsers = new ArrayList<>();

        jdbcTemplate.query(
            "SELECT ssn FROM user_profile",
            rs -> {
                String ssn = rs.getString("ssn");
                if (bankUsers.containsKey(ssn)) {
                    commonUsers.add(bankUsers.get(ssn));
                }
            }
        );

        model.addAttribute("users", commonUsers);
        return "fragments/common-users :: userTable";
    }

    // Simple DTO
    static class UserRow {
        public String ssn;
        public String firstName;
        public String lastName;
        public String email;

        public UserRow(String ssn, String firstName, String lastName, String email) {
            this.ssn = ssn;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
    }
}