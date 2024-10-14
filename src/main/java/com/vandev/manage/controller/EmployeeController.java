package com.vandev.manage.controller;

import com.vandev.manage.dto.EmployeeDTO;
import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.dto.ScoreDTO;
import com.vandev.manage.pojo.Score;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.ImageServiceImpl;
import com.vandev.manage.serviceImpl.ScoreServiceImpl;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    @GetMapping("/user/employees")
    public String listEmployees(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        Page<EmployeeDTO> employeePage = employeeServiceImpl.getPagedEmployees(Pageable.ofSize(size).withPage(page));
        List<String> departmentNames = employeePage.getContent().stream()
                .map(employee -> {
                    logger.info(employee.getDepartmentId().toString());
                    if (employee.getDepartmentId() != null) {
                        DepartmentDTO department = departmentServiceImpl.getDepartmentById(employee.getDepartmentId());
                        return department != null ? department.getName() : "N/A";
                    }
                    return "N/A";
                }).toList();
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("departmentNames", departmentNames);
        model.addAttribute("searchUrl", "/user/employees/search");
        return "employee/index";
    }

    @GetMapping("/user/employees/search")
    public String searchEmployees(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<EmployeeDTO> employeePage = employeeServiceImpl.searchByFullName(query, Pageable.ofSize(size).withPage(page));
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("query", query);
        return "employee/index";
    }

    @GetMapping("/admin/employees/create")
    public String createEmployee(Model model) {
        model.addAttribute("departments", departmentServiceImpl.getAllDepartments());
        model.addAttribute("employee", new EmployeeDTO());
        return "employee/create";
    }

    @PostMapping("/admin/employees/create")
    public String createEmployee(@ModelAttribute EmployeeDTO employeeDTO,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("departmentId") String departmentId,
                                 RedirectAttributes redirectAttributes) {
        String imagePath = imageServiceImpl.saveImageWithTimestamp(imageFile);
        if (imagePath.equals("Failed to upload image")) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload image");
            return "redirect:/user/employees";
        }
        employeeDTO.setImage(imagePath);
        if (departmentId == null || departmentId.isEmpty()) {
            employeeDTO.setDepartmentId(null);
        } else {
            DepartmentDTO departmentDTO = departmentServiceImpl.getDepartmentById(Integer.parseInt(departmentId));
            employeeDTO.setDepartmentId(departmentDTO.getId());
        }
        employeeServiceImpl.createEmployee(employeeDTO);
        return "redirect:/user/employees";
    }

    @GetMapping("/user/employees/view/{id}")
    public String viewEmployee(@PathVariable("id") Integer id, Model model) {
        EmployeeDTO employeeDTO = employeeServiceImpl.getEmployeeById(id);
        List<ScoreDTO> scoreDTOs = scoreServiceImpl.getScoreByEmployeeId(id);
        int totalAchievements = (int) scoreDTOs.stream().filter(score -> score.isType()).count();
        int totalDisciplines = (int) scoreDTOs.stream().filter(score -> !score.isType()).count();
        int rewardScore = totalAchievements - totalDisciplines;
        employeeDTO.setImage("/" + employeeDTO.getImage());
        Integer departmentId=employeeDTO.getDepartmentId();
        if(departmentId != null){
            DepartmentDTO departmentDTO=departmentServiceImpl.getDepartmentById(departmentId);
            model.addAttribute("departmentName", departmentDTO.getName());
        }
        else{
            model.addAttribute("departmentName", "N/A");
        }
        model.addAttribute("totalAchievements", totalAchievements);
        model.addAttribute("totalDisciplines", totalDisciplines);
        model.addAttribute("rewardScore", rewardScore);
        model.addAttribute("employee", employeeDTO);
        return "employee/detail";
    }


    @GetMapping("/admin/employees/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        EmployeeDTO employeeDTO = employeeServiceImpl.getEmployeeById(id);
        employeeDTO.setImage("/" + employeeDTO.getImage());
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("departments", departmentServiceImpl.getAllDepartments());
        return "employee/edit";
    }

    @PostMapping("/admin/employees/edit/{id}")
    public String updateEmployee(@PathVariable("id") Integer id,
                                 @ModelAttribute EmployeeDTO employeeDTO,
                                 @RequestParam("image") MultipartFile imageFile,
                                 @RequestParam("departmentId") String departmentId,
                                 RedirectAttributes redirectAttributes) {
        EmployeeDTO existingEmployeeDTO = employeeServiceImpl.getEmployeeById(id);
        String newImagePath = imageServiceImpl.updateImage(existingEmployeeDTO.getImage(), imageFile);
        employeeDTO.setImage(newImagePath);
        if (newImagePath.equals("Failed to update image")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update employee or handle image file.");
            return "redirect:/user/employees";
        }
        if (departmentId == null || departmentId.isEmpty()) {
            employeeDTO.setDepartmentId(null);
        } else {
            DepartmentDTO departmentDTO = departmentServiceImpl.getDepartmentById(Integer.parseInt(departmentId));
            employeeDTO.setDepartmentId(departmentDTO.getId());
        }
        employeeServiceImpl.updateEmployee(id, employeeDTO);
        return "redirect:/user/employees";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/employees/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            EmployeeDTO employeeDTO = employeeServiceImpl.getEmployeeById(id);
            String imagePath = employeeDTO.getImage();
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
