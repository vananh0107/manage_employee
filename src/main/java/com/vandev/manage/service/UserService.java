package com.vandev.manage.service;

import com.vandev.manage.pojo.UserSystem;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserSystem> findByUsername(String username);
    UserSystem getCurrentUser();
    UserSystem createUser(UserSystem user, Integer employeeId);
    void save(UserSystem user);
    boolean usernameExists(String username);
    List<UserSystem> findAllUsers();
    void deleteUserById(Integer id);
    void setActive(Integer id, boolean active);
    void updateUser(UserSystem user, Boolean active, Integer employeeId);
    UserSystem getUserById(Integer id);
}
