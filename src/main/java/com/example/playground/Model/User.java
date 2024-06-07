package com.example.playground.Model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String role;

    public User(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    public boolean isAdmin()
    {
        return "Admin".equalsIgnoreCase(role);
    }
}

    // Getters and setters (생략)
