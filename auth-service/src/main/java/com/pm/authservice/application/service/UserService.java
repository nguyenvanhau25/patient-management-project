package com.pm.authservice.application.service;

import com.pm.authservice.application.dto.UserLogin;
import com.pm.authservice.application.mapper.UserMapper;
import com.pm.authservice.domain.Role;
import com.pm.authservice.domain.User;
import com.pm.authservice.infrastructure.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final  PasswordEncoder  passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() == null){
            user.setRole(Role.USER); // mặc định USER
        }
        return userRepository.save(user);
    }

    public String delete(UUID uuid){
        User user = userRepository.findById(uuid).orElse(null);
        if(user == null){
            return "not found";
        }
        userRepository.delete(user);
        return "success";
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

}
