package com.yiban.springthreadpool.controller;

import com.yiban.springthreadpool.entites.CustomerMixInfo;
import com.yiban.springthreadpool.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.controller
 * @className CustomerController
 * @date 2025/11/4
 * @description
 */
@Slf4j
@RestController
public class CustomerController {
    @Autowired
    @Qualifier("customerServiceImpl")
    private CustomerService customerService;

    @GetMapping("/customer/info")
    public String getCustomerInfo() {
        CustomerMixInfo customerMixInfo = customerService.findCustomerByCompletableFuture();
        return customerMixInfo.toString();
    }
}
