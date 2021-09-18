package com.flipper.responses;

import com.flipper.models.User;
import lombok.Data;

@Data
public class LoginResponse {

    public boolean error;
    public String message;
    public String jwt;
    public User user;
}
