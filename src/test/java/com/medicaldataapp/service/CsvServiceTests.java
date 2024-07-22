package com.medicaldataapp.service;

import com.medicaldataapp.entity.TimeSeriesData;
import com.medicaldataapp.repository.TimeSeriesDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CsvServiceTests {

    @Autowired
    private CsvService csvService;

    @Autowired
    private TimeSeriesDataRepository repository;

    @Test
    @Transactional
    public void testProcessCsv() throws Exception {
        // 模拟一个 CSV 文件内容
        String csvContent = "2024-07-15 20:00:00,1,72.0,120.0,80.0,98.0,36.5\n" +
                "2024-07-15 20:05:00,2,75.0,130.0,85.0,97.0,37.0";

        // 处理 CSV 内容
        List<TimeSeriesData> result = csvService.processCsv(csvContent);

        // 验证数据
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);

        // 获取数据库中的数据进行验证
        List<TimeSeriesData> dbResult = repository.findAll();

        assertThat(dbResult.get(0).getTimestamp()).isEqualTo(Timestamp.valueOf("2024-07-15 20:00:00"));
        assertThat(dbResult.get(0).getPatientId()).isEqualTo(1L);
        assertThat(dbResult.get(0).getHeartRate()).isEqualTo(72.0);
        assertThat(dbResult.get(0).getBloodPressureSystolic()).isEqualTo(120.0);
        assertThat(dbResult.get(0).getBloodPressureDiastolic()).isEqualTo(80.0);
        assertThat(dbResult.get(0).getOxygenLevel()).isEqualTo(98.0);
        assertThat(dbResult.get(0).getBodyTemperature()).isEqualTo(36.5);

        assertThat(dbResult.get(1).getTimestamp()).isEqualTo(Timestamp.valueOf("2024-07-15 20:05:00"));
        assertThat(dbResult.get(1).getPatientId()).isEqualTo(2L);
        assertThat(dbResult.get(1).getHeartRate()).isEqualTo(75.0);
        assertThat(dbResult.get(1).getBloodPressureSystolic()).isEqualTo(130.0);
        assertThat(dbResult.get(1).getBloodPressureDiastolic()).isEqualTo(85.0);
        assertThat(dbResult.get(1).getOxygenLevel()).isEqualTo(97.0);
        assertThat(dbResult.get(1).getBodyTemperature()).isEqualTo(37.0);
    }
}
