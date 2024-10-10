package com.vandev.manage.serviceImpl;

import com.vandev.manage.dto.UserSystemDTO;
import com.vandev.manage.mapper.UserMapper;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername_UserExists_Success() {
        UserSystem user = new UserSystem();
        String username = "vanh";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<UserSystem> result = userServiceImpl.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_UserNotFound_ReturnsEmpty() {
        String username = "vanh";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserSystem> result = userServiceImpl.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void save_User_Success() {
        UserSystem user = new UserSystem();

        userServiceImpl.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void usernameExists_UserExists_ReturnsTrue() {
        String username = "vanh";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new UserSystem()));

        boolean result = userServiceImpl.usernameExists(username);

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void usernameExists_UserNotFound_ReturnsFalse() {
        String username = "vanh";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userServiceImpl.usernameExists(username);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getPagedUsers_Success() {
        Pageable pageable = PageRequest.of(0, 8);
        Page<UserSystem> page = new PageImpl<>(List.of(new UserSystem(), new UserSystem()));

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<UserSystemDTO> result = userServiceImpl.getPagedUsers(pageable);

        assertEquals(2, result.getTotalElements());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void deleteUserById_UserExists_Success() {
        Integer userId = 1;

        userServiceImpl.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUserById_UserExists_Success() {
        UserSystem user = new UserSystem();
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(new UserSystemDTO());

        UserSystemDTO result = userServiceImpl.getUserById(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userServiceImpl.getUserById(userId);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser_Success() {
        UserSystem user = new UserSystem();
        user.setActive(false);
        Integer employeeId = 1;
        Employee employee = new Employee();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        userServiceImpl.updateUser(1, true, employeeId);

        assertTrue(user.getActive());
        assertEquals(employee, user.getEmployee());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_InvalidEmployeeId_ThrowsException() {
        UserSystem user = new UserSystem();
        Integer employeeId = 1;

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userServiceImpl.updateUser(1, true, employeeId);
        });

        assertEquals("Invalid Employee ID: " + employeeId, exception.getMessage());
        verify(userRepository, never()).save(any(UserSystem.class));
    }

    @Test
    void updateUser_EmployeeIdIsNull_Success() {
        UserSystem user = new UserSystem();
        user.setActive(false);
        Integer employeeId = null;

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userServiceImpl.updateUser(1, true, employeeId);

        assertTrue(user.getActive());
        assertNull(user.getEmployee());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void searchByUserName_Success() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<UserSystem> page = new PageImpl<>(List.of(new UserSystem(), new UserSystem()));

        when(userRepository.findByUsernameContainingIgnoreCase("vanh", pageable)).thenReturn(page);
        when(userMapper.toDTO(any(UserSystem.class))).thenReturn(new UserSystemDTO());

        Page<UserSystemDTO> result = userServiceImpl.searchByUserName("vanh", pageable);

        assertEquals(2, result.getTotalElements());
        verify(userRepository, times(1)).findByUsernameContainingIgnoreCase("vanh", pageable);
    }
}
