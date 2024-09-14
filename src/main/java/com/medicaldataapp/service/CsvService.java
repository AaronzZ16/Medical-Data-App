package com.medicaldataapp.service;

import com.medicaldataapp.entity.TimeSeriesData;
import com.medicaldataapp.repository.TimeSeriesDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CsvService {

    @Autowired
    private TimeSeriesDataRepository repository;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d H:mm");

    public List<TimeSeriesData> getAllData() {
        return repository.findAll();
    }

    public TimeSeriesData getDataById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<TimeSeriesData> processCsv(String csvContent) {
        List<TimeSeriesData> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("Timestamp")) {
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length < 7) {
                    continue;
                }
                try {
                    TimeSeriesData data = parseLine(fields);
                    repository.save(data);
                    dataList.add(data);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private TimeSeriesData parseLine(String[] fields) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(fields[0].trim(), dateTimeFormatter));
        String patientId = fields[1].trim();
        Optional<TimeSeriesData> existingData = repository.findByTimestampAndPatientId(timestamp, patientId);
        TimeSeriesData data;
        if (existingData.isPresent()) {
            data = existingData.get();
        } else {
            data = new TimeSeriesData();
            data.setTimestamp(timestamp);
            data.setPatientId(patientId);
        }
        data.setHeartRate(Double.parseDouble(fields[2].trim()));
        data.setBloodPressureSystolic(Double.parseDouble(fields[3].trim()));
        data.setBloodPressureDiastolic(Double.parseDouble(fields[4].trim()));
        data.setOxygenLevel(Double.parseDouble(fields[5].trim()));
        data.setBodyTemperature(Double.parseDouble(fields[6].trim()));
        return data;
    }

    public void deleteDataById(Long id) {
        if (isAdmin()) {
            repository.deleteById(id);
        } else {
            throw new SecurityException("You do not have permission to delete data.");
        }
    }

    public void deleteAllData() {
        if (isAdmin()) {
            repository.deleteAll();
        } else {
            throw new SecurityException("You do not have permission to delete data.");
        }
    }

    public void updateData(TimeSeriesData data) {
        if (isAdmin()) {
            repository.save(data);
        } else {
            throw new SecurityException("You do not have permission to edit data.");
        }
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }
}
