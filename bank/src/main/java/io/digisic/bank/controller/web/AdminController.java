package io.digisic.bank.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/registered-users")
    public String showRegisteredUsersPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("firstName", username); // temp fallback
        model.addAttribute("avatar", "admin.jpg"); // fallback image
        model.addAttribute("notifications", Collections.emptyList());
        return "registered-users";
    }
}