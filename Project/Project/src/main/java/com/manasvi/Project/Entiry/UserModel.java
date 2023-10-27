package com.manasvi.Project.Entiry;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserModel {
    @NotBlank(message = "FirstName should not be empty")
    private String username;

    @NotNull(message = "Email should not be empty")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "Password should not be empty")
    @Size(min = 5, message = "Password should be atleast 5 characters")
    private String password;

    private String roles;//ROLE_USER,ROLE_ADMIN

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private Status status;

    public UserModel() {
        super();

    }

    public UserModel(String username, String email, String password, String roles, String verificationCode, Status status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.verificationCode = verificationCode;
        this.status = status;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}