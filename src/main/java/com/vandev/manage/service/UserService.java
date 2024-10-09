package com.vandev.manage.service;

import com.vandev.manage.pojo.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserService {
    Optional<UserSystem> findByUsername(String username);
    UserSystem getCurrentUser();
    void save(UserSystem user);
    boolean usernameExists(String username);
    Page<UserSystem> getPagedUsers(Pageable pageable);
    void deleteUserById(Integer id);
    void updateUser(UserSystem user, Boolean active, Integer employeeId);
    UserSystem getUserById(Integer id);
    Page<UserSystem> searchByUserName(String username,Pageable pageable);
}
