package com.example.tasklisttwo.service.impl;

import com.example.tasklisttwo.service.AuthService;
import com.example.tasklisttwo.web.dto.auth.JwtRequest;
import com.example.tasklisttwo.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }

}
