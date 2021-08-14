package com.register.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {

    private List<StockInfo> stockList;
    private List<Company> companyList;
    private Company company;
    private StockInfo stock;

}
