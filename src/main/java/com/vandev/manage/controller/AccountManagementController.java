package com.vandev.manage.controller;

import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AccountManagementController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AccountManagementController(UserServiceImpl userServiceImpl) {
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
//    @GetMapping("/account-management")
//    public String showAccountManagementPage(Model model) {
//        // Bạn có thể thêm bất kỳ thông tin nào cần thiết vào model
//        return "account-management"; // Trả về trang account-management.html
//    }
}
