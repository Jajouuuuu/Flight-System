package com.flight_system.customer_service.service;

import com.flight_system.customer_service.model.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAll();
    
    Customer getById(Long id);
    
    Customer getByEmail(String email);
    
    Customer create(Customer customer);
    
    void delete(Long id);
    
    Customer update(Long id, Customer updatedCustomer);
    
    List<Customer> searchCustomersByLastName(String lastName);
}
