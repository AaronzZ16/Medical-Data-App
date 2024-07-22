package com.medicaldataapp.repository;

import com.medicaldataapp.entity.TimeSeriesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface TimeSeriesDataRepository extends JpaRepository<TimeSeriesData, Long> {
    Optional<TimeSeriesData> findByTimestampAndPatientId(Timestamp timestamp, String patientId);
}
