package com.googe.ssadm.sc.dto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RepairTableDTO {
    private long id;

    private long orderId;

    private Date tableCreateDate;

    private Date tableCloseDate;

    private boolean tableClosed;

    private BigDecimal discount;

    private String partOrWork;

    private List<PartDTO> partDTOList;

    public RepairTableDTO() {
    }

    public RepairTableDTO(long id, long orderId, Date tableCreateDate, Date tableCloseDate,
                          boolean tableClosed, BigDecimal discount, List<PartDTO> partDTOList) {
        this.id = id;
        this.orderId = orderId;
        this.tableCreateDate = tableCreateDate;
        this.tableCloseDate = tableCloseDate;
        this.tableClosed = tableClosed;
        this.discount = discount;
        this.partDTOList = partDTOList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Date getTableCreateDate() {
        return tableCreateDate;
    }

    public void setTableCreateDate(Date tableCreateDate) {
        this.tableCreateDate = tableCreateDate;
    }

    public Date getTableCloseDate() {
        return tableCloseDate;
    }

    public void setTableCloseDate(Date tableCloseDate) {
        this.tableCloseDate = tableCloseDate;
    }

    public boolean isTableClosed() {
        return tableClosed;
    }

    public void setTableClosed(boolean tableClosed) {
        this.tableClosed = tableClosed;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public List<PartDTO> getPartDTOList() {
        return partDTOList;
    }

    public void setPartDTOList(List<PartDTO> partDTOList) { this.partDTOList = partDTOList; }

    public String getPartOrWork() { return partOrWork; }

    public void setPartOrWork(String partOrWork) { this.partOrWork = partOrWork; }
}
