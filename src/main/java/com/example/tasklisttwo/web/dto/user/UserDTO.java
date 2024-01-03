package com.example.tasklisttwo.web.dto.user;


import com.example.tasklisttwo.web.validation.OnCreate;
import com.example.tasklisttwo.web.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDTO {

    @NotNull(message = "Id must be not null!",
            groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Name must be not null!",
            groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name must be smaller than 255",
            groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @NotNull(message = "Username must be not null!",
            groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Username must be smaller than 255",
            groups = {OnUpdate.class, OnCreate.class})
    @Email(message = "Username must have email format!",
            groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null!",
            groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null!",
            groups = OnCreate.class)
    private String passwordConfirmation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

}
