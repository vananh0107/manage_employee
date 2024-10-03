package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.repository.UserRepository;
import com.vandev.manage.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Optional<UserSystem> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public UserSystem getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username).orElse(null);
        }
        return null; // hoặc có thể ném ra ngoại lệ nếu không tìm thấy người dùng
    }
    @Override
    @Transactional
    public UserSystem createUser(UserSystem user, Integer employeeId) {
        UserSystem currentUser = getCurrentUser(); // Lấy người dùng hiện tại đang đăng nhập

        // Kiểm tra nếu người dùng hiện tại là admin
        if (!currentUser.getRole().equals("admin")) {
            throw new SecurityException("Chỉ admin mới có quyền tạo người dùng.");
        }

        // Nếu có employeeId được cung cấp, gán nhân viên cho người dùng
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("ID nhân viên không hợp lệ: " + employeeId));
            user.setEmployee(employee);
        }

        // Lưu người dùng mới
        return userRepository.save(user);
    }
    @Override
    public void save(UserSystem user) {
        System.out.println("Saving user: " + user.getUsername());
        userRepository.save(user);
    }
    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent(); // Kiểm tra xem người dùng đã tồn tại hay chưa
    }
    // Phương thức lấy tất cả người dùng
    @Override
    public List<UserSystem> findAllUsers() {
        return userRepository.findAll(); // Trả về danh sách tất cả người dùng
    }

    // Phương thức xóa người dùng theo ID
    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id); // Xóa người dùng theo ID
    }

    @Override
    public void setActive(Integer id, boolean active) {
        Optional<UserSystem> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setActive(active); // Cập nhật trạng thái active cho người dùng
            userRepository.save(user); // Lưu thay đổi
        });
    }
}
