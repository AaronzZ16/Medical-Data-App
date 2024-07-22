CREATE TABLE IF NOT EXISTS time_series_data (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   timestamp DATETIME NOT NULL,
   value DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS medical_data (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   timestamp DATETIME NOT NULL,
   value DOUBLE NOT NULL
);
