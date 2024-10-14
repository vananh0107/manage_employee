package com.vandev.manage.controller;

import com.vandev.manage.dto.DepartmentSummaryDTO;
import com.vandev.manage.dto.EmployeeSummaryDTO;
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
public class ScoreController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private ScoreServiceImpl scoreServiceImpl;

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @GetMapping("/user/scores")
    public String listScores(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "8") int size){
        Page<Score> scorePage = scoreServiceImpl.getPagedScores(Pageable.ofSize(size).withPage(page));
        List<Employee> employees = employeeServiceImpl.getAllEmployees();
        String searchUrl="/user/scores/search";
        model.addAttribute("searchUrl",searchUrl);
        model.addAttribute("employees", employees);
        model.addAttribute("scorePage", scorePage);
        return "score/index";
    }
    @GetMapping("/user/scores/search")
    public String searchScores(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Score> scorePage = scoreServiceImpl.searchScoreByEmployeeFullName(query,Pageable.ofSize(size).withPage(page));
        model.addAttribute("scorePage", scorePage);
        model.addAttribute("query", query);
        return "score/index";
    }
    @PostMapping("/admin/scores/create")
    public String createScore(@ModelAttribute Score score,
                              RedirectAttributes redirectAttributes) {
        try {
            scoreServiceImpl.createScore(score);
            redirectAttributes.addFlashAttribute("message", "Score created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create score.");
        }

        return "redirect:/user/scores";
    }
    @GetMapping("/user/scores/view/{id}")
    public String viewScore(@PathVariable("id") Integer id, Model model) {
        Score score = scoreServiceImpl.getScoreById(id);
        if (score == null) {
            return "redirect:/user/scores";
        }
        model.addAttribute("score", score);
        return "score/detail";
    }

    @GetMapping("/admin/scores/edit/{id}")
    public String editScore(@PathVariable("id") Integer id, Model model) {
        Score score = scoreServiceImpl.getScoreById(id);
        if (score == null) {
            return "redirect:/user/scores";
        }

        List<Employee> employees = employeeServiceImpl.getAllEmployees();
        model.addAttribute("score", score);
        model.addAttribute("employees", employees);
        return "score/edit";
    }

    @PostMapping("/admin/scores/edit/{id}")
    public String updateScore(@PathVariable("id") Integer id,
                              @ModelAttribute Score score) {
        scoreServiceImpl.updateScore(id, score);
        return "redirect:/user/scores";
    }
    @GetMapping("/admin/scores/delete/{id}")
    public String deleteScore(@PathVariable("id") Integer id) {
        scoreServiceImpl.deleteScore(id);
        return "redirect:/user/scores";
    }
    @GetMapping("/user/scores/employee/{id}")
    public String getEmployeeScores(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeServiceImpl.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/user/employees";
        }
        List<Score> scores = scoreServiceImpl.getScoreByEmployeeId(id);
        model.addAttribute("employee", employee);
        model.addAttribute("scores", scores);
        return "score/employee";
    }
    @GetMapping("/user/scores/department/{id}")
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
    @GetMapping("/admin/scores/create")
    public String createEmployee(Model model) {
        model.addAttribute("employees", employeeServiceImpl.getAllEmployees());
        model.addAttribute("score", new Score());
        return "score/create";
    }
    @GetMapping("/user/scores/rank")
    public String getSummary(@RequestParam(defaultValue = "individual") String viewType,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        if ("individual".equals(viewType)) {
            Page<EmployeeSummaryDTO> employeeSummary = scoreServiceImpl.getEmployeeSummary(page);
            model.addAttribute("employeeSummary", employeeSummary);
        } else {
            Page<DepartmentSummaryDTO> departmentSummary = scoreServiceImpl.getDepartmentSummary(page);
            model.addAttribute("departmentSummary", departmentSummary);
        }

        model.addAttribute("viewType", viewType);

        return "score/summary";
    }
}