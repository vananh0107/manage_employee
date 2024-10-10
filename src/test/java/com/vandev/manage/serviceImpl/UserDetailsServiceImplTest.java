package com.vandev.manage.serviceImpl;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExistsAndActive_Success() {
        String username = "john_doe";
        UserSystem userSystem = new UserSystem();
        userSystem.setUsername(username);
        userSystem.setPassword("password");
        userSystem.setActive(true);
        userSystem.setRole("ROLE_USER");

        when(userService.findByUsername(username)).thenReturn(Optional.of(userSystem));

        UserDetails result = userDetailsServiceImpl.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        String username = "john_doe";

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername(username);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserNotActive_ThrowsInternalAuthenticationServiceException() {
        String username = "john_doe";
        UserSystem userSystem = new UserSystem();
        userSystem.setUsername(username);
        userSystem.setPassword("password");
        userSystem.setActive(false);
        userSystem.setRole("ROLE_USER");

        when(userService.findByUsername(username)).thenReturn(Optional.of(userSystem));

        Exception exception = assertThrows(InternalAuthenticationServiceException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername(username);
        });

        assertEquals("User account is not active", exception.getMessage());
        verify(userService, times(1)).findByUsername(username);
    }
}
