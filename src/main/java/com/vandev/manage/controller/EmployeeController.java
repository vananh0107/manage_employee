package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.ImageServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @Autowired
    private ScoreServiceImpl scoreServiceImpl;

    @Autowired
    private ImageServiceImpl imageServiceImpl;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("image");
    }
    @GetMapping("/user/employees")
    public String listEmployees(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        Page<Employee> employeePage = employeeServiceImpl.getPagedEmployees(Pageable.ofSize(size).withPage(page));
        for (Employee employee : employeePage.getContent()) {
            String imagePath = employee.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fileName = "/"+imagePath;
                employee.setImage(fileName);
            }
        }
        String searchUrl="/user/employees/search";
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("searchUrl",searchUrl);
        return "employee/index";
    }

    @GetMapping("/user/employees/search")
    public String searchEmployees(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Employee> employeePage = employeeServiceImpl.searchByFullName(query,Pageable.ofSize(size).withPage(page));
        for (Employee employee : employeePage.getContent()) {
            String imagePath = employee.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fileName = "/"+imagePath;
                employee.setImage(fileName);
            }
        }
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("query", query);
        return "employee/index";
    }

    @GetMapping("/admin/employees/create")
    public String createEmployee(Model model) {
        model.addAttribute("departments", departmentServiceImpl.getAllDepartments());
        model.addAttribute("employee", new Employee());
        return "employee/create";
    }

    @PostMapping("/admin/employees/create")
    public String createEmployee(@ModelAttribute Employee employee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("departmentId") String departmentId,
                                 RedirectAttributes redirectAttributes) {
            String imagePath = imageServiceImpl.saveImageWithTimestamp(imageFile);
            if(imagePath.equals("Failed to upload image")) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload image");
                return "redirect:/user/employees";
            }
            employee.setImage(imagePath);
            if (departmentId == null || departmentId.isEmpty()) {
                employee.setDepartment(null);
            } else {
                Department department = departmentServiceImpl.getDepartmentById(Integer.parseInt(departmentId));
                employee.setDepartment(department);
            }
            employeeServiceImpl.createEmployee(employee);
            return "redirect:/user/employees";
    }

    @GetMapping("/user/employees/view/{id}")
    public String viewEmployee(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeServiceImpl.getEmployeeById(id);
        List<Score> scores = scoreServiceImpl.getScoreByEmployeeId(id);
        int totalAchievements = (int) scores.stream().filter(score -> score.isType()).count();
        int totalDisciplines = (int) scores.stream().filter(score -> !score.isType()).count();
        int rewardScore = totalAchievements - totalDisciplines;
        employee.setImage("/"+employee.getImage());
        model.addAttribute("totalAchievements", totalAchievements);
        model.addAttribute("totalDisciplines", totalDisciplines);
        model.addAttribute("rewardScore", rewardScore);
        model.addAttribute("employee", employee);
        return "employee/detail";
    }

    @GetMapping("/admin/employees/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeServiceImpl.getEmployeeById(id);
        employee.setImage("/"+employee.getImage());
        model.addAttribute("employee", employee);
        List<Department> departments = departmentServiceImpl.getAllDepartments();
        model.addAttribute("departments", departments);
        return "employee/edit";
    }

    @PostMapping("/admin/employees/edit/{id}")
    public String updateEmployee(@PathVariable("id") Integer id,
                                 @ModelAttribute Employee employee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("departmentId") String departmentId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
            Employee existingEmployee = employeeServiceImpl.getEmployeeById(id);
            String newImagePath = imageServiceImpl.updateImage(existingEmployee.getImage(),imageFile);
            employee.setImage(newImagePath);
            if(newImagePath.equals("Failed to update image")){
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to update employee or handle image file.");
                return "redirect:/user/employees";
            }
            if (departmentId == null || departmentId.isEmpty()) {
                employee.setDepartment(null);
            } else {
                Department department = departmentServiceImpl.getDepartmentById(Integer.parseInt(departmentId));
                employee.setDepartment(department);
            }
            employeeServiceImpl.updateEmployee(id, employee);
            return "redirect:/user/employees";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/employees/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeServiceImpl.getEmployeeById(id);
            String imagePath = employee.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                Path filePath = Paths.get(imagePath);
                Files.deleteIfExists(filePath);
            }

            employeeServiceImpl.deleteEmployee(id);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete image file.");
        }
        return "redirect:/user/employees";
    }
}
