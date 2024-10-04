package com.vandev.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NonActiveController {

    @GetMapping("/non-active")
    public String showNonActivePage(Model model) {
        return "non-active";
    }
}