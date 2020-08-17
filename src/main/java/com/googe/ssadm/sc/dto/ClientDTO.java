package com.googe.ssadm.sc.dto;

import com.googe.ssadm.sc.annotation.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ClientDTO {
    private long id;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String name;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String surname;
    @Email
    private String email;
    @Phone
    private String mobile;

    public ClientDTO(long id, String name, String surname, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile = mobile;
    }

    public ClientDTO() {
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
}
