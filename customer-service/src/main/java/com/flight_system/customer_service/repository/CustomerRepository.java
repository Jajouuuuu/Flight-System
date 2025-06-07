package com.flight_system.customer_service.repository;

import com.flight_system.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
}

