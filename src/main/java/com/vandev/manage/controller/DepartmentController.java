package com.vandev.manage.controller;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private ScoreServiceImpl scoreServiceImpl;

    @GetMapping("/user/departments")
    public String listDepartments(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Page<Department> departmentPage = departmentServiceImpl.findPaginated(page, size);
        model.addAttribute("departmentPage", departmentPage);
        model.addAttribute("employeeServiceImpl", employeeServiceImpl);
        return "department/index";
    }
    @PostMapping("/admin/departments/create")
    public String createDepartment(@ModelAttribute Department department,
                                   @RequestParam(value = "employeeIds", required = false) List<Integer> employeeIds,
                                   RedirectAttributes redirectAttributes) {
        departmentServiceImpl.createDepartment(department);

        if (employeeIds != null && !employeeIds.isEmpty()) {
            List<Employee> employees = employeeServiceImpl.findAllById(employeeIds);
            employees.forEach(employee -> employee.setDepartment(department));
            employeeServiceImpl.saveAll(employees);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Department created successfully!");

        return "redirect:/user/departments";
    }
    @GetMapping("/user/departments/view/{id}")
    public String viewDepartment(@PathVariable("id") Integer id, Model model) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        List<Employee> employees = employeeServiceImpl.getEmployeesByDepartment(department);
        int totalAchievements = 0;
        int totalDisciplines = 0;
        int rewardScore = 0;

        for (Employee employee : employees) {
            List<Score> scores = scoreServiceImpl.getScoreByEmployeeId(employee.getId());
            totalAchievements += (int) scores.stream().filter(score -> score.isType()).count();
            totalDisciplines += (int) scores.stream().filter(score -> !score.isType()).count();
        }
        rewardScore = (int) totalAchievements-totalDisciplines;
        model.addAttribute("department", department);
        model.addAttribute("employees", employees);
        model.addAttribute("totalAchievements", totalAchievements);
        model.addAttribute("totalDisciplines", totalDisciplines);
        model.addAttribute("rewardScore", rewardScore);

        return "department/detail";
    }
    @GetMapping("/admin/departments/create")
    public String createEmployee(Model model) {
        List<Employee> employeesWithoutDepartment = employeeServiceImpl.getEmployeesWithoutDepartment();
        model.addAttribute("employees", employeesWithoutDepartment);
        model.addAttribute("department", new Department());
        return "department/create";
    }
    @GetMapping("/admin/departments/edit/{id}")
    public String editDepartment(@PathVariable("id") Integer id, Model model) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        List<Employee> employeesInDepartment = employeeServiceImpl.getEmployeesByDepartment(department);
        List<Employee> employeesWithoutDepartment = employeeServiceImpl.getEmployeesWithoutDepartment();

        model.addAttribute("department", department);
        model.addAttribute("employeesInDepartment", employeesInDepartment);
        model.addAttribute("employeesWithoutDepartment", employeesWithoutDepartment);

        return "department/edit";
    }

    @PostMapping("/admin/departments/edit/{id}")
    public String updateDepartment(@PathVariable("id") Integer id,
                                   @ModelAttribute Department department,
                                   @RequestParam(value = "currentEmployeeIds", required = false) List<Integer> currentEmployeeIds,
                                   @RequestParam(value = "newEmployeeIds", required = false) List<Integer> newEmployeeIds,
                                   RedirectAttributes redirectAttributes) {
        Department existingDepartment = departmentServiceImpl.getDepartmentById(id);
        existingDepartment.setName(department.getName());
        departmentServiceImpl.updateDepartment(id,existingDepartment);

        List<Employee> currentEmployees = employeeServiceImpl.getEmployeesByDepartment(existingDepartment);
        for (Employee employee : currentEmployees) {
            if (currentEmployeeIds == null || !currentEmployeeIds.contains(employee.getId())) {
                employee.setDepartment(null);
            }
        }
        employeeServiceImpl.saveAll(currentEmployees);
        if (newEmployeeIds != null && !newEmployeeIds.isEmpty()) {
            List<Employee> newEmployees = employeeServiceImpl.findAllById(newEmployeeIds);
            newEmployees.forEach(employee -> employee.setDepartment(existingDepartment));
            employeeServiceImpl.saveAll(newEmployees);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
        return "redirect:/user/departments";
    }
    @GetMapping("/admin/departments/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        if (department != null) {
            List<Employee> employees = employeeServiceImpl.getEmployeesByDepartment(department);
            employees.forEach(employee -> employee.setDepartment(null));
            employeeServiceImpl.saveAll(employees);
            departmentServiceImpl.deleteDepartment(id);
        }
        return "redirect:/user/departments";
    }
}

