package com.medicaldataapp.repository;

import com.medicaldataapp.entity.model.MedicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalDataRepository extends JpaRepository<MedicalData, Long> {
}
