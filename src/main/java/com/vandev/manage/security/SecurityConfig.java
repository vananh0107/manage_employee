package com.vandev.manage.security;

import com.vandev.manage.serviceImpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFailure authenticationFailure;
    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,AuthenticationFailure authenticationFailure) {
        this.userDetailsService = userDetailsService;
        this.authenticationFailure = authenticationFailure;
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register","/non-active", "/resources/**", "/static/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ cho phép admin truy cập
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Cho phép user và admin truy cập
                        .anyRequest().authenticated() // Các trang khác yêu cầu đăng nhập
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true) // Redirect to home after login
                        .failureHandler(authenticationFailure)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Configure CSRF with cookie-based repository
                );

        return http.build();
    }
}
