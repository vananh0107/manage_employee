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

    // Chỉ admin mới được phép truy cập
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/account-management")
    public String showAccountManagement(Model model) {
        List<UserSystem> users = userServiceImpl.findAllUsers(); // Phương thức lấy danh sách tất cả người dùng
        model.addAttribute("users", users);
        return "account-management"; // Trả về trang account-management.html
    }

    @PostMapping("/account-management/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userServiceImpl.deleteUserById(id); // Phương thức xóa người dùng theo ID
        return "redirect:/admin/account-management"; // Quay lại trang quản lý tài khoản
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Integer id) {
        userServiceImpl.setActive(id, true); // Kích hoạt người dùng
        return "redirect:/admin/users"; // Chuyển hướng về danh sách người dùng
    }
    // Show the edit user form
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model,
                                   @RequestParam(defaultValue = "") String searchQuery,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        // Get user by ID
        UserSystem user = userServiceImpl.getUserById(id);
        model.addAttribute("user", user);

        // Paginate and search employees
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeServiceImpl.getPagedEmployees(searchQuery, pageable);

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("searchQuery", searchQuery);

        return "user-edit";
    }

    // Handle form submission for editing user
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Integer id,
                             @RequestParam("active") Boolean active,
                             @RequestParam("employeeId") Integer employeeId) {
        UserSystem user = userServiceImpl.getUserById(id);

        // Update the user with active status and assigned employee
        userServiceImpl.updateUser(user, active, employeeId);

        return "redirect:/admin/account-management";
    }
}
