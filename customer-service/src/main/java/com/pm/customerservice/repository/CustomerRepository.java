package com.pm.customerservice.repository;

import com.pm.customerservice.module.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByAddress(String address);
    boolean existsByAddressAndIdNot(String address, UUID id);
    Customer findByEmail(String email);
}
