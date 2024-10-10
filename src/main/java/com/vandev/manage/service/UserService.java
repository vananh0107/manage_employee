package com.vandev.manage.service;

import com.vandev.manage.dto.UserSystemDTO;
import com.vandev.manage.pojo.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserService {
    Optional<UserSystem> findByUsername(String username);
    void save(UserSystem user);
    boolean usernameExists(String username);
    Page<UserSystemDTO> getPagedUsers(Pageable pageable);
    void deleteUserById(Integer id);
    void updateUser(Integer id, Boolean active, Integer employeeId);
    UserSystemDTO getUserById(Integer id);
    Page<UserSystemDTO> searchByUserName(String username, Pageable pageable);
}
