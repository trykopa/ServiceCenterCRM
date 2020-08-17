package com.googe.ssadm.sc.dto;

import java.util.Date;

public class NoteDTO {
    private long id;
    private String message;
    private Date createdAt;

    public NoteDTO() {
    }
    public NoteDTO(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
