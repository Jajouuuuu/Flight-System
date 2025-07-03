package com.flight_system.accounting_service.repository;

import com.flight_system.accounting_service.model.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    List<AccountTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM AccountTransaction t WHERE YEAR(t.transactionDate) = :year")
    List<AccountTransaction> findByYear(@Param("year") int year);

    @Query("SELECT t FROM AccountTransaction t WHERE YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    List<AccountTransaction> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT t FROM AccountTransaction t WHERE t.referenceId = :bookingNumber")
    List<AccountTransaction> findByBookingNumber(@Param("bookingNumber") String bookingNumber);

    List<AccountTransaction> findByTransactionType(String transactionType);
}
