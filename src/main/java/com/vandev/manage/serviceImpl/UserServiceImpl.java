package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.UserSystem; // Đây là lớp User của bạn
import com.vandev.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User; // Đảm bảo import đúng lớp User
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSystem userSystem = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Sử dụng User để tạo UserDetails
        return User.withUsername(userSystem.getUsername()) // Sử dụng userSystem thay vì appUser
                .password(userSystem.getPassword())
                .roles("USER") // Gán role mặc định cho user
                .build();
    }
}
