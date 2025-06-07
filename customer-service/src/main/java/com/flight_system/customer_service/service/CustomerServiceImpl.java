package com.flight_system.customer_service.service;

import com.flight_system.customer_service.model.Customer;
import com.flight_system.customer_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Le client n'a pas été trouvé : " + id));
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer updatedCustomer) {
        Customer existing = getById(id);
        existing.setFirstName(updatedCustomer.getFirstName());
        existing.setLastName(updatedCustomer.getLastName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setLoyaltyStatus(updatedCustomer.getLoyaltyStatus());
        return customerRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}

