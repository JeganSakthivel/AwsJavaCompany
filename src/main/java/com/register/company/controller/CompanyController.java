package com.register.company.controller;

import com.register.company.entity.Company;
import com.register.company.respository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/company")
    public Company saveEmployee(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @GetMapping("/company/{code}")
    public Company getEmployee(@PathVariable("code") String companyCode) {
        return companyRepository.find(companyCode);
    }
}



