package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSystem userSystem = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!userSystem.getActive()) {
            throw new InternalAuthenticationServiceException("User account is not active");
        }
        return User.withUsername(userSystem.getUsername())
                .password(userSystem.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(userSystem.getRole())) // Role mặc định là "USER"
                .build();
    }
}
