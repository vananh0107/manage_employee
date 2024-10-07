package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private ScoreServiceImpl scoreServiceImpl;

    @GetMapping
    public String listScores(Model model) {
        List<Score> scores = scoreServiceImpl.getAllScoresSortedByDate();
        List<Employee> employees = employeeServiceImpl.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("scores", scores);
        return "score/index";
    }
    @PostMapping("/create")
    public String createScore(@ModelAttribute Score score,
                              RedirectAttributes redirectAttributes) {
        try {
            scoreServiceImpl.createScore(score);
            redirectAttributes.addFlashAttribute("message", "Score created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create score.");
        }

        return "redirect:/scores";
    }
}