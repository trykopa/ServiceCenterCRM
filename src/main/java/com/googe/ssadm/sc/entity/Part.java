package com.googe.ssadm.sc.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_part")
public class Part implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String partNo;
    private String partDesc;
    private String serialNo;

    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal entryPrice;
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal recommendedPrice;

    private boolean partOrWork;
    private boolean onStock;

    @ManyToOne
    @JoinColumn(name = "repair_id")
    private RepairTable repairTable;

    @ManyToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;


}
