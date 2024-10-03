package com.vandev.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDenied {
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";  // Trả về trang access-denied.html
    }
}
