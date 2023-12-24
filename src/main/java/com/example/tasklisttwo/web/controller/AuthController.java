package com.example.tasklisttwo.web.controller;

import com.example.tasklisttwo.model.user.User;
import com.example.tasklisttwo.service.AuthService;
import com.example.tasklisttwo.service.UserService;
import com.example.tasklisttwo.web.dto.auth.JwtRequest;
import com.example.tasklisttwo.web.dto.auth.JwtResponse;
import com.example.tasklisttwo.web.dto.user.UserDTO;
import com.example.tasklisttwo.web.mappers.UserMapper;
import com.example.tasklisttwo.web.validation.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(AuthService authService, UserService userService, UserMapper userMapper) {
        this.authService = authService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDTO register(
            @Validated(OnCreate.class) @RequestBody UserDTO dto
    ) {
        System.out.println("Im here!");
//        User user = userMapper.toEntity(dto);
//        User createdUser = userService.create(user);
//        return userMapper.toDTO(createdUser);
        return null;
    }

    @GetMapping("/test")
    public void test(){
        System.out.println("test");
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
