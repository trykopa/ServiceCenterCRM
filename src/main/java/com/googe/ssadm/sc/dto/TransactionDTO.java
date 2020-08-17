package com.googe.ssadm.sc.dto;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDTO {
    private long id;
    @NotNull
    @Size(min = 1, max = 10, message = "Min length is 1 max is 10")
    private String shortDescription;
    @NotNull
    @Size(min = 1, max = 100, message = "Min length is 1 max is 100")
    private String description;
    private Date createdat;
    @NotNull
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal amount;
    private boolean debit;
    private String currency;
    private String author;

    public TransactionDTO() {
    }

    public TransactionDTO(
            long id ,
            String description ,
            String shortDescription ,
            Date createdat ,
            BigDecimal amount ,
            boolean debit ,
            String currency) {
        this.id = id;
        this.description = description;
        this.shortDescription = shortDescription;
        this.createdat = createdat;
        this.amount = amount;
        this.debit = debit;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDescription() { return shortDescription; }

    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdat;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdat = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean getDebit() {
        return debit;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }
}
