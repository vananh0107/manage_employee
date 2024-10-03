package com.vandev.manage.validator;

import com.vandev.manage.pojo.UserSystem;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSystem.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSystem user = (UserSystem) target;

        // Kiểm tra tên đăng nhập
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.rejectValue("username", "NotEmpty", "Tên đăng nhập không được để trống");
        }

        // Kiểm tra mật khẩu
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "NotEmpty", "Mật khẩu không được để trống");
        } else if (user.getPassword().length() < 8) {
            errors.rejectValue("password", "Size", "Mật khẩu phải lớn hơn hoặc bằng 8 ký tự");
        }
    }
}
