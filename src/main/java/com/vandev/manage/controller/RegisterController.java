package com.vandev.manage.controller;

import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegisterController {

    @Autowired
    private  UserServiceImpl userServiceImpl;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserSystem());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserSystem user, BindingResult result, Model model) {
        if (userServiceImpl.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Tên đăng nhập đã tồn tại");
        }
        if (user.getPassword().length()<8) {
            result.rejectValue("password", "error.password", "Mật khẩu phải lớn hợn hoặc bằng 8 kí tự");
        }
        if (result.hasErrors()) {
            return "register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setActive(false);
        userServiceImpl.save(user);
        return "redirect:/login";
    }

}