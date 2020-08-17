package com.googe.ssadm.sc.dto;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PartDTO {
    private long id;

    @NotNull
    private String partNo;
    @NotNull
    private String partDesc;
    private String serialNo;

    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal entryPrice;
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal recommendedPrice;

    private boolean partOrWork;
    private boolean onStock;

    private long supplierId;

    public PartDTO() {
    }

    public PartDTO(long id, String partNo, String partDesc, String serialNo, BigDecimal entryPrice,
                   BigDecimal recommendedPrice, boolean partOrWork, boolean onStock, long supplierId) {
        this.id = id;
        this.partNo = partNo;
        this.partDesc = partDesc;
        this.serialNo = serialNo;
        this.entryPrice = entryPrice;
        this.recommendedPrice = recommendedPrice;
        this.partOrWork = partOrWork;
        this.supplierId = supplierId;
        this.onStock = onStock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPartDesc() {
        return partDesc;
    }

    public void setPartDesc(String partDesc) {
        this.partDesc = partDesc;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(BigDecimal entryPrice) {
        this.entryPrice = entryPrice;
    }

    public BigDecimal getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(BigDecimal recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }

    public boolean isPartOrWork() {
        return partOrWork;
    }

    public void setPartOrWork(boolean partOrWork) {
        this.partOrWork = partOrWork;
    }

    public long getSupplierId() { return supplierId; }

    public void setSupplierId(long supplierId) { this.supplierId = supplierId; }

    public boolean isOnStock() { return onStock; }

    public void setOnStock(boolean onStock) { this.onStock = onStock; }

}
