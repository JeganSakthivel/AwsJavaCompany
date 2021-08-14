package com.register.company.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockInfo {

    private int stockId;
    private String companyCode;
    private BigDecimal stockPrice;
    private Date date;
    private Time time;

}
