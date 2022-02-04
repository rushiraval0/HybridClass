package com.example.hybridclass;

import java.util.List;

public class User {

    private String email;
    private String name;
    private String password;
    private String confirmPassword;
    private String role;// 0 = student, 1 = teacher
    private List<String> classList;

    public User(String email, String name, String password, String confirmPassword, String role, List<String> classList) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.classList = classList;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public User(){

    }
    public User(String email, String name, String password, String confirmPassword, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
