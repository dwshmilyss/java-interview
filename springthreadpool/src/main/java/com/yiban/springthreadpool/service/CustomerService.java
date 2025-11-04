package com.yiban.springthreadpool.service;

import com.yiban.springthreadpool.entites.CustomerMixInfo;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.service
 * @interfaceName CostomerService
 * @date 2025/11/4
 * @description
 */
public interface CustomerService {
    CustomerMixInfo findCustomerByCompletableFuture();
}
