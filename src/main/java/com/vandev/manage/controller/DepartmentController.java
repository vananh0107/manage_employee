package com.vandev.manage.controller;

import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.dto.EmployeeDTO;
import com.vandev.manage.dto.ScoreDTO;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ScoreServiceImpl scoreService;

    @GetMapping("/user/departments")
    public String listDepartments(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Page<DepartmentDTO> departmentPage = departmentService.findPaginated(page, size);
        model.addAttribute("searchUrl", "/user/departments/search");
        model.addAttribute("departmentPage", departmentPage);
        return "department/index";
    }

    @GetMapping("/user/departments/search")
    public String searchDepartments(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<DepartmentDTO> departmentPage = departmentService.searchByName(query, Pageable.ofSize(size).withPage(page));
        model.addAttribute("departmentPage", departmentPage);
        model.addAttribute("query", query);
        return "department/index";
    }

    @PostMapping("/admin/departments/create")
    public String createDepartment(@ModelAttribute DepartmentDTO departmentDTO,
                                   @RequestParam(value = "employeeIds", required = false) List<Integer> employeeIds,
                                   Model model) {
        try {
            DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);

            if (employeeIds != null && !employeeIds.isEmpty()) {
                List<EmployeeDTO> employees = employeeService.findAllById(employeeIds);
                employees.forEach(employee -> employee.setDepartmentId(createdDepartment.getId()));
                employeeService.saveAll(employees);
            }
            return "redirect:/user/departments";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "department/create";
        }
    }

    @GetMapping("/user/departments/view/{id}")
    public String viewDepartment(@PathVariable("id") Integer id, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        if (departmentDTO == null) {
            return "redirect:/user/departments";
        }

        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(departmentDTO).stream()
                .toList();
        int totalAchievements = 0;
        int totalDisciplines = 0;

        for (EmployeeDTO employee : employees) {
            List<ScoreDTO> scores = scoreService.getScoreByEmployeeId(employee.getId());
            totalAchievements += (int) scores.stream().filter(score -> score.isType()).count();
            totalDisciplines += (int) scores.stream().filter(score -> !score.isType()).count();
        }

        int rewardScore = totalAchievements - totalDisciplines;
        model.addAttribute("department", departmentDTO);
        model.addAttribute("employees", employees);
        model.addAttribute("totalAchievements", totalAchievements);
        model.addAttribute("totalDisciplines", totalDisciplines);
        model.addAttribute("rewardScore", rewardScore);

        return "department/detail";
    }

    @GetMapping("/admin/departments/create")
    public String createDepartmentForm(Model model) {
        List<EmployeeDTO> employeesWithoutDepartment = employeeService.getEmployeesWithoutDepartment();
        model.addAttribute("employees", employeesWithoutDepartment);
        model.addAttribute("department", new DepartmentDTO());
        return "department/create";
    }

    @GetMapping("/admin/departments/edit/{id}")
    public String editDepartment(@PathVariable("id") Integer id, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        if (departmentDTO == null) {
            return "redirect:/user/departments";
        }

        List<EmployeeDTO> employeesInDepartment = employeeService.getEmployeesByDepartment(departmentDTO);
        List<EmployeeDTO> employeesWithoutDepartment = employeeService.getEmployeesWithoutDepartment();

        model.addAttribute("department", departmentDTO);
        model.addAttribute("employeesInDepartment", employeesInDepartment);
        model.addAttribute("employeesWithoutDepartment", employeesWithoutDepartment);

        return "department/edit";
    }

    @PostMapping("/admin/departments/edit/{id}")
    public String updateDepartment(@PathVariable("id") Integer id,
                                   @ModelAttribute DepartmentDTO departmentDTO,
                                   @RequestParam(value = "currentEmployeeIds", required = false) List<Integer> currentEmployeeIds,
                                   @RequestParam(value = "newEmployeeIds", required = false) List<Integer> newEmployeeIds) {
        DepartmentDTO existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment != null) {
            existingDepartment.setName(departmentDTO.getName());
            departmentService.updateDepartment(id, existingDepartment);

            List<EmployeeDTO> currentEmployees = employeeService.getEmployeesByDepartment(existingDepartment);
            currentEmployees.forEach(employee -> {
                if (currentEmployeeIds == null || !currentEmployeeIds.contains(employee.getId())) {
                    employee.setDepartmentId(null);
                }
            });
            employeeService.saveAll(currentEmployees);

            if (newEmployeeIds != null && !newEmployeeIds.isEmpty()) {
                List<EmployeeDTO> newEmployees = employeeService.findAllById(newEmployeeIds);
                newEmployees.forEach(employee -> employee.setDepartmentId(departmentDTO.getId()));
                employeeService.saveAll(newEmployees);
            }
        }

        return "redirect:/user/departments";
    }

    @GetMapping("/admin/departments/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Integer id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        if (department != null) {
            List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(department);
            employees.forEach(employee -> employee.setDepartmentId(null));
            employeeService.saveAll(employees);
            departmentService.deleteDepartment(id);
        }
        return "redirect:/user/departments";
    }
}
