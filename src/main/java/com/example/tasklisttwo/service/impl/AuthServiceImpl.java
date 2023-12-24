package com.example.tasklisttwo.service.impl;

import com.example.tasklisttwo.model.exception.AccessDeniedException;
import com.example.tasklisttwo.model.user.User;
import com.example.tasklisttwo.service.AuthService;
import com.example.tasklisttwo.service.UserService;
import com.example.tasklisttwo.web.dto.auth.JwtRequest;
import com.example.tasklisttwo.web.dto.auth.JwtResponse;
import com.example.tasklisttwo.web.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserService userService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        String username = loginRequest.getUsername();
        User user = userService.getByUsername(username);
        Long userId = user.getId();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );
        jwtResponse.setId(userId);
        jwtResponse.setUsername(username);
        jwtResponse.setAccessToken(
                jwtTokenProvider.createAccessToken(
                        userId, username, user.getRoles()
                )
        );
        jwtResponse.setRefreshToken(
                jwtTokenProvider.createRefreshToken(
                        userId, username
                )
        );
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshTokens(refreshToken);
    }

}
