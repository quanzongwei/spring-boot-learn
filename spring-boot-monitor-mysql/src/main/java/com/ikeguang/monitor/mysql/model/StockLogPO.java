package com.ikeguang.monitor.mysql.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ Author: keguang
 * @ Date: 2019/7/25 10:50
 * @ version: v1.0.0
 * @ description:
 */

@Entity
@Table(name = "qzw_stock_log_2")
@Data
@DynamicInsert
@DynamicUpdate
public class StockLogPO {

    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false, columnDefinition = "decimal(9,4) NOT NULL COMMENT '单价'")
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "int(11) NOT NULL COMMENT '股数'")
    private Integer amount;

    @Column(nullable = false, columnDefinition = "varchar(16) NOT NULL COMMENT '日志类型,buy sale auto_up 以及其他'")
    private String logType;

    @Column(nullable = false, columnDefinition = "varchar(32) NOT NULL COMMENT '股票名称'")
    private String stockName;

    @Column(nullable = false, columnDefinition = "varchar(32) NOT NULL COMMENT '股票代码'")
    private String stockCode;

    @Column(nullable = false, columnDefinition = "varchar(128) NOT NULL DEFAULT '描述'")
    private String description;

    @Column(nullable = false, columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private Date createTime;

    @Column(nullable = false, columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    private Date updateTime;


    @Transient
    private Integer pageNum = 0;
}
