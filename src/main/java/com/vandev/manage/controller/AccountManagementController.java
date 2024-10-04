package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import com.vandev.manage.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AccountManagementController {

    private final UserServiceImpl userServiceImpl;
    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public AccountManagementController(UserServiceImpl userServiceImpl, EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/account-management")
    public String showAccountManagement(Model model) {
        List<UserSystem> users = userServiceImpl.findAllUsers();
        model.addAttribute("users", users);
        return "account-management";
    }

    @PostMapping("/account-management/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userServiceImpl.deleteUserById(id);
        return "redirect:/admin/account-management";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Integer id) {
        userServiceImpl.setActive(id, true);
        return "redirect:/admin/users";
    }
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model,
                                   @RequestParam(defaultValue = "") String searchQuery,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        UserSystem user = userServiceImpl.getUserById(id);
        model.addAttribute("user", user);

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeServiceImpl.getPagedEmployees(searchQuery, pageable);

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("searchQuery", searchQuery);

        return "user-edit";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Integer id,
                             @RequestParam("active") Boolean active,
                             @RequestParam("employeeId") Integer employeeId) {
        UserSystem user = userServiceImpl.getUserById(id);

        userServiceImpl.updateUser(user, active, employeeId);

        return "redirect:/admin/account-management";
    }
}
