package com.example.tasklisttwo.service;

import com.example.tasklisttwo.web.dto.auth.JwtRequest;
import com.example.tasklisttwo.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
