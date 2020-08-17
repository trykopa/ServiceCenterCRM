package com.googe.ssadm.sc.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_repair")
public class RepairTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date tableCreateDate;

    @Temporal(TemporalType.DATE)
    private Date tableCloseDate;

    private boolean tableClosed;

    @Column(precision = 5, scale = 2)
    private BigDecimal discount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "part_id")
    private List<Part> partList;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "repair")
    private Order order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Part> getPartList() {
        return partList;
    }

    public void setPartList(List<Part> partList) {
        this.partList = partList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
