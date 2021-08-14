package com.register.company.respository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.register.company.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyRepository {

    @Autowired
    private DynamoDBMapper mapper;

    public Company save(Company company) {
        mapper.save(company);
        return find(company.getCompanyCode());
    }

    public Company find(String companyCode) {
        return mapper.load(Company.class, companyCode);
    }

    public String deleteCompany(String companyCode) {
        Company company = mapper.load(Company.class, companyCode);
        mapper.delete(company);
        Company deletedCompany = mapper.load(Company.class, companyCode);
        if (deletedCompany == null ) {
            return "Deleted Successfully";
        } else  {
            return "Delete failed";
        }
    }

    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withSelect(Select.ALL_ATTRIBUTES);

    public List<Company> findAll(){
        return mapper.scan(Company.class, scanExpression);
    }


}
