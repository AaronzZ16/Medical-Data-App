package com.medicaldataapp.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_data") // 添加此注解以明确指定表名
public class MedicalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Double value;

    // 无参构造函数
    public MedicalData() {
    }

    // 全参构造函数
    public MedicalData(LocalDateTime timestamp, Double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MedicalData{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
