package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.repository.UserRepository;
import com.vandev.manage.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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
        return null;
    }
    @Override
    @Transactional
    public UserSystem createUser(UserSystem user, Integer employeeId) {
        UserSystem currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("admin")) {
            throw new SecurityException("Chỉ admin mới có quyền tạo người dùng.");
        }
        return userRepository.save(user);
    }
    @Override
    public void save(UserSystem user) {
        System.out.println("Saving user: " + user.getUsername());
        userRepository.save(user);
    }
    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    @Override
    public List<UserSystem> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public void setActive(Integer id, boolean active) {
        Optional<UserSystem> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setActive(active);
            userRepository.save(user);
        });
    }
    @Override
    public UserSystem getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
    @Override
    public void updateUser(UserSystem user, Boolean active, Integer employeeId) {
        user.setActive(active);

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee ID: " + employeeId));
            user.setEmployee(employee);
        }

        userRepository.save(user);
    }
}
