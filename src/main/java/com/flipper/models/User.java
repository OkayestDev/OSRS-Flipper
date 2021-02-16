package com.flipper.models;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class User {
    private UUID id;
    private String displayName;
    private String email;
    private String password;
    private Timestamp updatedAt;
    private Timestamp createdAt;

    public void setEmailAndPassword(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
