package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @Autowired
    private ScoreServiceImpl scoreServiceImpl;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("image");
    }
    @GetMapping
    public String listEmployees(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        Page<Employee> employeePage = employeeServiceImpl.getPagedEmployees(Pageable.ofSize(size).withPage(page));
        for (Employee employee : employeePage.getContent()) {
            String imagePath = employee.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fileName = "/"+imagePath.substring(imagePath.lastIndexOf("\\") + 1);
                employee.setImage(fileName);
            }
        }
        model.addAttribute("employeePage", employeePage);
        return "employee/index";
    }

    @GetMapping("/create")
    public String createEmployee(Model model) {
        model.addAttribute("departments", departmentServiceImpl.getAllDepartments());
        model.addAttribute("employee", new Employee());
        return "employee/create";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute Employee employee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("departmentId") String departmentId,
                                 RedirectAttributes redirectAttributes) {
        try {
            String imagePath = saveImageWithTimestamp(imageFile);
            employee.setImage(imagePath);
            if (departmentId == null || departmentId.isEmpty()) {
                employee.setDepartment(null);
            } else {
                Department department = departmentServiceImpl.getDepartmentById(Integer.parseInt(departmentId));
                employee.setDepartment(department);
            }
            employeeServiceImpl.createEmployee(employee);
            return "redirect:/employees";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload image");
            return "redirect:/employees";
        }
    }

    private String saveImageWithTimestamp(MultipartFile imageFile) throws IOException {
        String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
        String customFileName = System.currentTimeMillis() + extension;
        String uploadDir = "C:/Users/anhbv/code/manage_employee/src/main/resources/upload_images/images/";
        Path filePath = Paths.get(uploadDir + customFileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "images/"+customFileName;
    }

    @GetMapping("/view/{id}")
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeServiceImpl.getEmployeeById(id);
        model.addAttribute("employee", employee);
        List<Department> departments = departmentServiceImpl.getAllDepartments();
        model.addAttribute("departments", departments);
        return "employee/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable("id") Integer id,
                                 @ModelAttribute Employee employee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("departmentId") String departmentId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            Employee existingEmployee = employeeServiceImpl.getEmployeeById(id);
            if (!imageFile.isEmpty()) {
                if (existingEmployee.getImage() != null && !existingEmployee.getImage().isEmpty()) {
                    Path oldImagePath = Paths.get(existingEmployee.getImage());
                    Files.deleteIfExists(oldImagePath);
                }
                String newImagePath = saveImageWithTimestamp(imageFile);
                employee.setImage(newImagePath);
            } else {
                employee.setImage(existingEmployee.getImage());
            }
            if (departmentId == null || departmentId.isEmpty()) {
                employee.setDepartment(null);
            } else {
                Department department = departmentServiceImpl.getDepartmentById(Integer.parseInt(departmentId));
                employee.setDepartment(department);
            }
            employeeServiceImpl.updateEmployee(id, employee);
            return "redirect:/employees";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update employee or handle image file.");
            return "redirect:/employees";
        }
    }

    @PostMapping("/delete/{id}")
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
        return "redirect:/employees";
    }
}
