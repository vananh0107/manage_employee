package com.vandev.manage.controller;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private ScoreServiceImpl scoreServiceImpl;

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @GetMapping
    public String listScores(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "8") int size){
        Page<Score> scorePage = scoreServiceImpl.getPagedScores(Pageable.ofSize(size).withPage(page));
        List<Employee> employees = employeeServiceImpl.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("scorePage", scorePage);
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
    @GetMapping("/view/{id}")
    public String viewScore(@PathVariable("id") Integer id, Model model) {
        Score score = scoreServiceImpl.getScoreById(id);
        if (score == null) {
            return "redirect:/scores";
        }
        model.addAttribute("score", score);
        return "score/detail";
    }

    @GetMapping("/edit/{id}")
    public String editScore(@PathVariable("id") Integer id, Model model) {
        Score score = scoreServiceImpl.getScoreById(id);
        if (score == null) {
            return "redirect:/scores";
        }

        List<Employee> employees = employeeServiceImpl.getAllEmployees();
        model.addAttribute("score", score);
        model.addAttribute("employees", employees);
        return "score/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateScore(@PathVariable("id") Integer id,
                              @ModelAttribute Score score) {
        scoreServiceImpl.updateScore(id, score);
        return "redirect:/scores";
    }
    @PostMapping("/delete/{id}")
    public String deleteScore(@PathVariable("id") Integer id) {
        scoreServiceImpl.deleteScore(id);
        return "redirect:/scores";
    }
    @GetMapping("/employee/{id}")
    public String getEmployeeScores(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeServiceImpl.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/employees";
        }
        List<Score> scores = scoreServiceImpl.getScoreByEmployeeId(id);
        model.addAttribute("employee", employee);
        model.addAttribute("scores", scores);
        return "score/employee";
    }
    @GetMapping("/department/{id}")
    public String viewDepartmentScores(@PathVariable("id") Integer id, Model model) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        List<Employee> employees = employeeServiceImpl.getEmployeesByDepartment(department);
        List<Score> scores = new ArrayList<>();
        for (Employee employee : employees) {
            List<Score> employeeScores = scoreServiceImpl.getScoreByEmployeeId(employee.getId());
            scores.addAll(employeeScores);
        }
        model.addAttribute("department", department);
        model.addAttribute("scores", scores);
        return "score/department";
    }
}