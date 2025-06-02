package com.example.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String reservationId;
    
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    private String paymentMethod;
    
    private LocalDateTime paymentDate;
    
    private String transactionId;
    
    @Column(length = 1000)
    private String paymentDetails;
    
    @Version
    private Long version;
    
    @PrePersist
    protected void onCreate() {
        paymentDate = LocalDateTime.now();
    }
} 