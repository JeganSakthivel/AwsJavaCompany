package com.register.company.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "company")
public class Company implements Serializable {

    @NotBlank(message = "companyCode is Mandatory")
    @DynamoDBHashKey  (attributeName = "companyCode")
    private String companyCode;

    @NotBlank(message = "companyName is Mandatory")
    @DynamoDBAttribute
    private String companyName;

    @NotBlank(message = "companyCeo is Mandatory")
    @DynamoDBAttribute
    private String companyCeo;

    @NotNull(message = "companyTurnOver is Mandatory")
    @Min(value = 100000000, message = "companyTurnOver must be greater than or equal to 10 Crore")
    @DynamoDBAttribute
    private Long companyTurnOver;

    @NotBlank(message = "companyWebsite is Mandatory")
    @DynamoDBAttribute
    private String companyWebsite;

    @NotBlank(message = "stockExchange is Mandatory")
    @DynamoDBAttribute
    private String stockExchange;

    @DynamoDBIgnore
    private StockInfo stockInfo;
}
