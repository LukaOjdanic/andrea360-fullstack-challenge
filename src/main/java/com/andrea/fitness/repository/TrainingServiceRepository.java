package com.andrea.fitness.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andrea.fitness.model.TrainingService;

@Repository
public interface TrainingServiceRepository extends JpaRepository<TrainingService, UUID> {
    
}
