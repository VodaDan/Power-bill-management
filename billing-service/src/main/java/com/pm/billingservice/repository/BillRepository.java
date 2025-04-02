package com.pm.billingservice.repository;

import com.pm.billingservice.model.Bill;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findByCustomerId(@NotNull UUID customerId);
}
