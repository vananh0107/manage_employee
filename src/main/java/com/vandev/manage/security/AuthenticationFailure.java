package com.vandev.manage.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String redirectUrl;

        // Kiểm tra xem lỗi là do tài khoản chưa được kích hoạt
        if (exception.getMessage() != null && exception.getMessage().contains("User account is not active")) {
            redirectUrl = "/non-active"; // Redirect đến trang non-active
        } else {
            redirectUrl = "/login?error=true"; // Redirect đến trang đăng nhập với thông báo lỗi
        }

        // Redirect đến URL tương ứng
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}