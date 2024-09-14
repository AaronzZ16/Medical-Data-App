package com.medicaldataapp.controller;

import com.medicaldataapp.entity.User;
import com.medicaldataapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        // 确保在注册时设置用户角色
        user.setRole("USER"); // 强制设置为普通用户
        userService.save(user);
        return "redirect:/login";
    }
}
