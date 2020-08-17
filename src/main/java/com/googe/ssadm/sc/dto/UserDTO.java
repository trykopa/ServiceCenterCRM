package com.googe.ssadm.sc.dto;

import com.googe.ssadm.sc.annotation.Phone;
import com.googe.ssadm.sc.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserDTO {
    private long id;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String name;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String surname;
    @Email
    private String email;
    @Phone(message = "Invalid phone format, valid is - 012345678,012-345-5678,(012)-345-5678")
    private String mobile;
    @Size(min = 8, message = "Minimal length of password is 8 symbols")
    private String password;
    @Size(min = 8, message = "Minimal length of password is 8 symbols")
    private String passwordConfirm;

    private boolean active;

    private Set<Role> roles;

    public UserDTO(long id, String name, String surname, String email, String mobile, String password, String passwordConfirm) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public UserDTO() {
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordConfirm() {
        return this.passwordConfirm;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
