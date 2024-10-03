package com.vandev.manage.service;

import com.vandev.manage.pojo.UserSystem;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserSystem> findByUsername(String username);
    UserSystem getCurrentUser();
    UserSystem createUser(UserSystem user, Integer employeeId);
    public void save(UserSystem user);
    boolean usernameExists(String username);
    public List<UserSystem> findAllUsers();
    public void deleteUserById(Integer id);
    public void setActive(Integer id, boolean active);
}
