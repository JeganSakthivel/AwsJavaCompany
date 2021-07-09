package com.register.company.respository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.register.company.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository {

    @Autowired
    private DynamoDBMapper mapper;

    public Company save(Company company) {
        mapper.save(company);
        return company;
    }

    public Company find(String companyCode) {
        return mapper.load(Company.class, companyCode);
    }




}
