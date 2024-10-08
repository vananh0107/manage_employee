package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Locale;

@Controller
public class HomeController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;
    @GetMapping("/user")
    public String showTopEmployees(HttpServletRequest request, Model model) {
        List<Employee> topEmployees = employeeServiceImpl.getTop10Employees();
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("employees", topEmployees);
        return "home";
    }
    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request) {
        Locale locale = new Locale(lang);
        // Lưu ngôn ngữ vào session
        HttpSession session = request.getSession();
        session.setAttribute("currentLocale", locale);
        return "redirect:" + request.getHeader("Referer"); // Trở lại trang trước đó
    }
}
