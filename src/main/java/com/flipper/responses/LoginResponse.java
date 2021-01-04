package com.flipper.responses;

import com.flipper.models.User;

import lombok.Data;

@Data
public class LoginResponse {
    public String jwt;
    public User user;
}
