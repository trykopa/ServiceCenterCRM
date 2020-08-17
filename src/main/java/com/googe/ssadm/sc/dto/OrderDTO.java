package com.googe.ssadm.sc.dto;

import com.googe.ssadm.sc.annotation.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class OrderDTO {
    private long id;
    @NotNull
    private String serialNo;
    @NotNull
    private String modelName;
    @NotNull
    private String defectDescription;

    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String clientName;
    @NotNull
    @Size(min = 1, max = 50, message = "Min length is 1 max is 50")
    private String clientSurname;
    @NotNull
    @Email
    private String clientEmail;
    @Phone
    private String clientMobile;
    private String status;

    private Date createdDate;

    public OrderDTO(long id, String status, String serialNo, String modelName, String defectDescription,
                    String clientName, String clientSurname, String clientEmail, String clientMobile,
                    Date createdDate) {
        this.id = id;
        this.serialNo = serialNo;
        this.modelName = modelName;
        this.defectDescription = defectDescription;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientEmail = clientEmail;
        this.clientMobile = clientMobile;
        this.status = status;
        this.createdDate = createdDate;
    }

    public OrderDTO() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public long getId() {
        return this.id;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getDefectDescription() {
        return this.defectDescription;
    }

    public String getClientName() {
        return this.clientName;
    }

    public String getClientSurname() {
        return this.clientSurname;
    }

    public String getClientEmail() {
        return this.clientEmail;
    }

    public String getClientMobile() {
        return this.clientMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
