CREATE TABLE IF NOT EXISTS users (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(50) UNIQUE NOT NULL,
 password VARCHAR(255) NOT NULL,
 role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS time_series_data (
 bid BIGINT AUTO_INCREMENT PRIMARY KEY,
 timestamp DATETIME NOT NULL,
 heart_rate DOUBLE,
 blood_pressure_systolic DOUBLE,
 blood_pressure_diastolic DOUBLE,
 oxygen_level DOUBLE,
 body_temperature DOUBLE
);

CREATE TABLE IF NOT EXISTS medical_data (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 timestamp DATETIME NOT NULL,
 value DOUBLE NOT NULL
);
