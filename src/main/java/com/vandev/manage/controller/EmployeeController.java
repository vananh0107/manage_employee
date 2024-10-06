package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Department;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
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
    private ResourceLoader resourceLoader;

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
                int startIndex = imagePath.indexOf("images");
                String relativePath = imagePath.substring(startIndex).replace("\\", "/");
                employee.setImage("/"+relativePath);
            }
        }

        model.addAttribute("employeePage", employeePage);
        return "employee/index";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute Employee employee,
                                 @RequestParam("image") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            String imagePath = saveImageWithTimestamp(imageFile);
            employee.setImage(imagePath);
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
        String uploadDir = "F:/VietIds/manage_employee/src/main/resources/static/images/";
        Path filePath = Paths.get(uploadDir + customFileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + customFileName;
    }

    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeServiceImpl.getEmployeeById(id);
//        String imagePath = employee.getImage();
//        if (imagePath != null && !imagePath.isEmpty()) {
//            int startIndex = imagePath.indexOf("images");
//            String relativePath = imagePath.substring(startIndex).replace("\\", "/");
//            employee.setImage("/"+relativePath);
//        }
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

//    @PostMapping("/edit/{id}")
//    public String updateEmployee(@PathVariable("id") Integer id,
//                                 @ModelAttribute @Valid Employee employee,
//                                 BindingResult result,
//                                 @RequestParam("image") MultipartFile imageFile, // Xử lý cập nhật ảnh
//                                 Model model,
//                                 RedirectAttributes redirectAttributes) throws IOException {
//        if (result.hasErrors()) {
//            List<Department> departments = departmentServiceImpl.getAllDepartments();
//            model.addAttribute("departments", departments);
//            return "employee/edit";
//        }
//
//        if (!imageFile.isEmpty()) {
//            // Chuyển ảnh thành chuỗi base64 và lưu trữ
//            employee.setImage(imageFile.getBytes());
//        }
//
//        employeeServiceImpl.updateEmployee(id, employee);
//        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
//        return "redirect:/employees";
//    }

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
