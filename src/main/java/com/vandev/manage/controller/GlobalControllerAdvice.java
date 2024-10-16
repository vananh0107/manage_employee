package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String username = authentication.getName();
            Optional<UserSystem> userOptional = userServiceImpl.findByUsername(username);
            if (userOptional != null) {
                UserSystem user = userOptional.get();
                Employee employee = user.getEmployee();
                if (employee != null) {
                    model.addAttribute("employeeId", employee.getId());
                } else {
                    model.addAttribute("employeeNotAssigned", true);
                }
            }
            model.addAttribute("username", username);
        }
    }
}
