package com.vandev.manage.repository;

import com.vandev.manage.pojo.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSystem, Integer> {
    Optional<UserSystem> findByUsername(String username);
    Page<UserSystem> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}