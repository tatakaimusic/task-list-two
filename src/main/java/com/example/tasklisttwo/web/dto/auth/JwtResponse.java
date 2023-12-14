package com.example.tasklisttwo.web.dto.auth;

import java.util.Objects;

public class JwtResponse {

    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;

    public JwtResponse() {
    }

    public JwtResponse(Long id, String username, String excessToken, String refreshToken) {
        this.id = id;
        this.username = username;
        this.accessToken = excessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, accessToken, refreshToken);
    }

}
