package com.pm.authservice.application.service;

import com.pm.authservice.application.dto.LoginRequestDTO;
import com.pm.authservice.domain.Role;
import com.pm.authservice.domain.User;
import com.pm.authservice.infrastructure.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(User user) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (user.getPassword() == null || !user.getPassword().matches(regex)) {
            throw new IllegalArgumentException("Password phai co it nhat 8 ky tu, gom chu hoa, chu thuong, so va ky tu dac biet");
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean delete(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }

    public User updateUser(UUID id, User newData) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setEmail(newData.getEmail());
                    existing.setRole(newData.getRole());
                    if (newData.getPassword() != null) {
                        existing.setPassword(passwordEncoder.encode(newData.getPassword()));
                    }
                    return userRepository.save(existing);
                })
                .orElse(null);
    }

    public boolean resetPassword(LoginRequestDTO login) {
        User user = userRepository.findByEmail(login.getEmail()).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(login.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
