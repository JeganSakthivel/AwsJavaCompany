package com.register.company.entity;

import java.io.Serializable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "company")
public class Company implements Serializable {

    @DynamoDBHashKey  (attributeName = "companyCode")
    @DynamoDBAutoGeneratedKey
    private String companyCode;

    @DynamoDBAttribute
    private String companyName;

    @DynamoDBAttribute
    private String companyCeo;

    @DynamoDBAttribute
    private String companyTurnOver;

    @DynamoDBAttribute
    private String companyWebsite;

    @DynamoDBAttribute
    private String stockExchange;

}
