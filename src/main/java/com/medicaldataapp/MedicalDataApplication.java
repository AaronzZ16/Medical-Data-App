package com.medicaldataapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class MedicalDataApplication {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public static void main(String[] args) {
        SpringApplication.run(MedicalDataApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Active Spring Profile: " + activeProfile);
    }
}
