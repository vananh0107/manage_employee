package com.vandev.manage.controller;

import com.vandev.manage.pojo.UserSystem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserSystem());
        return "login";
    }
    @GetMapping("/non-active")
    public String showNonActivePage(Model model) {
        return "error/non-active";
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex, Model model) {
        return new ModelAndView("error/not-found", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
