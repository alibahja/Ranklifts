package com.example.ranklifts_java.models;

public class AuthRequest {
    private String username;
    private String email;
    private String password;

    // For login (email + password only)
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // For register (username + email + password)
    public AuthRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}