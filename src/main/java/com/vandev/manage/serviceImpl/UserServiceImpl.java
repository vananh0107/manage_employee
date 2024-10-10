package com.vandev.manage.serviceImpl;

import com.vandev.manage.dto.UserSystemDTO;
import com.vandev.manage.mapper.UserMapper;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.repository.UserRepository;
import com.vandev.manage.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Optional<UserSystem> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(UserSystem user) {
        userRepository.save(user);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public Page<UserSystemDTO> getPagedUsers(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 8);
        return userRepository.findAll(fixedPageable).map(userMapper::toDTO);
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserSystemDTO getUserById(Integer id) {
        UserSystem user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public void updateUser(Integer id, Boolean active, Integer employeeId) {
        UserSystem user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setActive(active);
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee ID: " + employeeId));
            user.setEmployee(employee);
        }
        else{
            user.setEmployee(null);
        }
        userRepository.save(user);
    }

    @Override
    public Page<UserSystemDTO> searchByUserName(String username, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable).map(userMapper::toDTO);
    }
}
