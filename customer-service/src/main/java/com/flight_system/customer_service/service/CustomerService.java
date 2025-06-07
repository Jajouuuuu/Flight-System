package com.flight_system.customer_service.service;

import com.flight_system.customer_service.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll();
    Customer getById(Long id);
    Customer create(Customer customer);
    Customer update(Long id, Customer customer);
    void delete(Long id);
}
