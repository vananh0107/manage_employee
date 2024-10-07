package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;
    @GetMapping("/")
    public String showTopEmployees(Model model) {
        List<Employee> topEmployees = employeeServiceImpl.getTop10Employees();
        model.addAttribute("employees", topEmployees);
        return "home";
    }
}
