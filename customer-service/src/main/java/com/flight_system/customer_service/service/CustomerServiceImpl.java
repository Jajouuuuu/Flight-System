package com.flight_system.customer_service.service;

import com.flight_system.customer_service.model.Customer;
import com.flight_system.customer_service.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + email));
    }

    @Override
    @Transactional
    public Customer create(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
        }
        Customer savedCustomer = customerRepository.save(customer);
        kafkaTemplate.send("customer-created", savedCustomer);
        return savedCustomer;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
        kafkaTemplate.send("customer-deleted", customer);
    }

    @Override
    @Transactional
    public Customer update(Long id, Customer updatedCustomer) {
        Customer existing = getById(id);
        
        if (!existing.getEmail().equals(updatedCustomer.getEmail()) 
            && customerRepository.existsByEmail(updatedCustomer.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + updatedCustomer.getEmail());
        }

        existing.setFirstName(updatedCustomer.getFirstName());
        existing.setLastName(updatedCustomer.getLastName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existing.setAddress(updatedCustomer.getAddress());
        
        Customer savedCustomer = customerRepository.save(existing);
        kafkaTemplate.send("customer-updated", savedCustomer);
        return savedCustomer;
    }

    @Override
    public List<Customer> searchCustomersByLastName(String lastName) {
        return customerRepository.findByLastNameContainingIgnoreCase(lastName);
    }
}

