package com.googe.ssadm.sc.dto;

import com.googe.ssadm.sc.annotation.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SupplierDTO {
    private long id;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String name;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String surname;
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String companyName;
    @Email
    private String email;
    @Phone(message = "Invalid phone format, valid is - 012345678,012-345-5678,(012)-345-5678")
    private String mobile;

    public SupplierDTO() {
    }

    public SupplierDTO(long id, String name, String surname, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile = mobile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
