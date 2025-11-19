package com.andrea.fitness.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andrea.fitness.model.Purchase;
import com.andrea.fitness.model.PurchaseStatus;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    // fund all successful purchases by member
        List<Purchase> findByMemberIdAndStatus(UUID memberId, PurchaseStatus status);

}
