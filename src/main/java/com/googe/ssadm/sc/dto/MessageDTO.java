package com.googe.ssadm.sc.dto;

import java.util.Date;

public class MessageDTO {
    private long id;
    private String text;
    private Date createdAt;
    private long fromUser;
    private long toUser;
    private boolean unread;

    public MessageDTO() {
    }

    public MessageDTO(long id , String text , Date createdAt , long toUser , long fromUser , boolean unread) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.unread = unread;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getToUser() {
        return toUser;
    }

    public void setToUser(long toUser) {
        this.toUser = toUser;
    }

    public long getFromUser() {
        return fromUser;
    }

    public void setFromUser(long fromUser) {
        this.fromUser = fromUser;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
