package com.vandev.manage.service;

import com.vandev.manage.pojo.UserSystem;
import java.util.Optional;

public interface UserService {
    Optional<UserSystem> findByUsername(String username);
}
