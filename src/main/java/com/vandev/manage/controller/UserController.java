package com.vandev.manage.controller;

import com.vandev.manage.dto.UserSystemDTO;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @GetMapping()
    public String showAccountManagement(Model model,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "8") int size) {
        Page<UserSystemDTO> users = userServiceImpl.getPagedUsers(Pageable.ofSize(size).withPage(page));
        model.addAttribute("users", users);
        List<Employee> availableEmployees = employeeServiceImpl.getEmployeesWithoutUser();
        String searchUrl = "/admin/users/search";
        model.addAttribute("searchUrl", searchUrl);
        model.addAttribute("availableEmployees", availableEmployees);
        return "user/index";
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<UserSystemDTO> users = userServiceImpl.searchByUserName(query, Pageable.ofSize(size).withPage(page));
        model.addAttribute("users", users);
        model.addAttribute("query", query);
        return "user/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userServiceImpl.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/view/{id}")
    public String viewUserDetails(@PathVariable Integer id, Model model) {
        UserSystemDTO user = userServiceImpl.getUserById(id);
        model.addAttribute("user", user);
        return "user/view";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model) {
        UserSystemDTO user = userServiceImpl.getUserById(id);
        model.addAttribute("user", user);
        List<Employee> employees = employeeServiceImpl.getEmployeesWithoutUser();
        model.addAttribute("employees", employees);
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Integer id,
                             @RequestParam("active") Boolean active,
                             @RequestParam(value="employeeId", required = false) Integer employeeId) {
        userServiceImpl.updateUser(id, active, employeeId);
        return "redirect:/admin/users";
    }
}
