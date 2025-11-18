package com.pm.authservice.interfaces;

import com.pm.authservice.application.dto.UserLogin;
import com.pm.authservice.application.service.UserService;
import com.pm.authservice.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "api for user ")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "get all user")
    public List<User> getUser(){
        return userService.getAll();
    }

    @PostMapping
    @Operation(summary = "create user")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete user")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id){
        String tel = userService.delete(id);
        return new ResponseEntity<>(tel, HttpStatus.OK);
    }

}
