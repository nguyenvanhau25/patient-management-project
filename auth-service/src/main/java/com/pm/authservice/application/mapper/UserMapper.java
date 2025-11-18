package com.pm.authservice.application.mapper;

import com.pm.authservice.application.dto.UserLogin;
import com.pm.authservice.domain.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserLogin userLogin(User user){
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(user.getEmail());
        userLogin.setPassword(user.getPassword());
        return userLogin;
    }
}
