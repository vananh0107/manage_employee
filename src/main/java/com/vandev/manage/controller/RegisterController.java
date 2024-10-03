package com.vandev.manage.controller;

import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.serviceImpl.UserServiceImpl;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@Slf4j
public class RegisterController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public RegisterController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserSystem());
        return "register"; // Trả về trang register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserSystem user, BindingResult result, Model model) {
        log.info("Đăng ký người dùng");

        // Kiểm tra nếu tên đăng nhập đã tồn tại
        if (userServiceImpl.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Tên đăng nhập đã tồn tại");
        }
        if (user.getPassword().length()<8) {
            result.rejectValue("password", "error.password", "Mật khẩu phải lớn hợn hoặc bằng 8 kí tự");
        }
        // Kiểm tra các lỗi khác nếu cần
        if (result.hasErrors()) {
            return "register"; // Nếu có lỗi, trả về lại trang đăng ký
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole("ROLE_USER"); // Gán vai trò mặc định là USER
        user.setActive(false);
        userServiceImpl.save(user); // Lưu người dùng vào cơ sở dữ liệu

        // Thêm thuộc tính model để thông báo thành công
        model.addAttribute("successMessage", "Bạn đã đăng ký thành công. Bạn sẽ được chuyển hướng sau 2 giây.");

        return "redirect:/login"; // Trả về lại trang đăng ký nhưng hiển thị thông báo
    }

}