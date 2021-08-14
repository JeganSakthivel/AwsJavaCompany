package com.register.company.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.company.entity.CodeListWrapper;
import com.register.company.entity.Company;
import com.register.company.entity.ResponseBean;
import com.register.company.entity.StockInfo;
import com.register.company.respository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController()
@RequestMapping("/")
public class CompanyController {

    Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Value("${STOCK_SERVICE_URI:http://localhost:8001/api/v1.0/market/stock/}")
    private String stockServiceHost;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("test")
    public String test(){
        return "Hello Comany";
    }

    @PostMapping("register")
    public ResponseEntity<Object> saveCompany(@Valid @RequestBody Company company) {
        logger.debug("saveCompany - company {} "+ company);
        if (companyRepository.find(company.getCompanyCode()) != null )  {
            return new ResponseEntity<>("Company Code already Exists",HttpStatus.CONFLICT);
        } else {
            Company storedCompany = companyRepository.save(company);
            logger.debug("storedCompany - {} "+ storedCompany);
            return new ResponseEntity<>(storedCompany, HttpStatus.CREATED);
        }
    }

    @GetMapping("info/{code}")
    public ResponseEntity<Object> getCompany(@PathVariable("code") String companyCode) {
        ResponseBean responseBean = new ResponseBean();
        logger.debug("getCompany - companyCode {} "+ companyCode);
        Company company = companyRepository.find(companyCode);
        StockInfo stockDetail = null ;
        if (company !=null) {
            responseBean.setCompany(company);
            logger.debug("stockServiceHost - "+ stockServiceHost);
            logger.debug("stockServiceHost uri - "+ stockServiceHost + companyCode);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(stockServiceHost + "{code}", String.class, createUriVariables(companyCode));
            if (responseEntity.getBody() != null ) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
                    if (jsonNode.has("stockId")) {

                        stockDetail = objectMapper.treeToValue(jsonNode,StockInfo.class);
                        logger.debug("stockDetail - " + responseEntity.getBody());
                        responseBean.getCompany().setStockInfo(stockDetail);
                    }
                } catch (JsonProcessingException e) {
                   logger.error(e.getMessage());
                }
            }
            return new ResponseEntity<>(responseBean.getCompany(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Company is not Available",HttpStatus.OK);
        }
    }

    @DeleteMapping("delete/{code}")
    public String deleteCompany(@PathVariable("code") String companyCode) {
        logger.debug("deleteCompany - companyCode {} " + companyCode);
        logger.debug("stockServiceHost uri - "+ stockServiceHost + "delete/"+companyCode);
        restTemplate.delete(stockServiceHost + "delete/{code}", createUriVariables(companyCode));
        return companyRepository.deleteCompany(companyCode);
    }

    @GetMapping("getall")
    public List<Company> getAllCompany(){
        logger.debug("getAllCompany - {} ");
        ResponseBean responseBean = new ResponseBean();
        List<Company> companyList = companyRepository.findAll();
        responseBean.setCompanyList(companyList);
        logger.debug("companyList - {} "+companyList);
        List<String> companyCodeList = new ArrayList<>();
        companyList.forEach( comp -> {
            companyCodeList.add(comp.getCompanyCode());
        });
        logger.debug("companyCodeList - {} "+companyCodeList);
        CodeListWrapper codeListWrapper = new CodeListWrapper();
        codeListWrapper.setCodeList(companyCodeList);
        logger.debug("stockServiceHost uri - "+ stockServiceHost + "getsCodeList");
        ResponseEntity<ResponseBean> responseEntity = restTemplate.postForEntity(
                stockServiceHost +"getCodeList",codeListWrapper,ResponseBean.class, new HashMap<>());
        List<Company> newCompanyList = responseBean.getCompanyList().stream().map(
                comp -> {
                    responseEntity.getBody().getStockList().forEach(
                        stk -> {
                            if(StringUtils.equals(comp.getCompanyCode(),stk.getCompanyCode())){
                                comp.setStockInfo(stk);
                            }
                        }
                    );
                    logger.debug("comp filtered - {} "+comp);
                    return comp;
                }
        ).collect(Collectors.toList());
        logger.debug("newCompanyList - {} "+newCompanyList);
        return newCompanyList;
    }

    private Map<String, String> createUriVariables(String companyCode) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("code", companyCode);
        return uriVariables;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object>  handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(error.getDefaultMessage());
        });
        logger.error("ValidationException - errors {} "+ errors);
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object>  handleAllExceptions(Exception ex) {
        List<String> errors = new ArrayList<>();
        errors.add( ex.getLocalizedMessage());
        logger.error("AllException - errors {} "+ errors);
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}



