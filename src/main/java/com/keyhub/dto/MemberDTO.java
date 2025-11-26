package com.keyhub.dto;

import java.sql.Timestamp;

public class MemberDTO {
    private String id;
    private String password;
    private String name;
    private String email;
    private String role;
    private Timestamp joinDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Timestamp getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }
}