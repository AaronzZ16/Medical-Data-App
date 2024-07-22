package com.medicaldataapp.service;

import com.medicaldataapp.MedicalDataApplication;
import com.medicaldataapp.entity.TimeSeriesData;
import com.medicaldataapp.repository.TimeSeriesDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MedicalDataApplication.class)
@Transactional
@Rollback
public class ChartServiceTests {

    @Autowired
    private ChartService chartService;

    @Autowired
    private TimeSeriesDataRepository repository;

    @Test
    public void testGenerateChart() throws Exception {
        // 添加一些测试数据
        TimeSeriesData data1 = new TimeSeriesData();
        data1.setPatientId("1");
        data1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        data1.setHeartRate(72.0);
        data1.setBloodPressureSystolic(120.0);
        data1.setBloodPressureDiastolic(80.0);
        data1.setOxygenLevel(98.0);
        data1.setBodyTemperature(36.5);
        repository.save(data1);

        TimeSeriesData data2 = new TimeSeriesData();
        data2.setPatientId("2");
        data2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        data2.setHeartRate(75.0);
        data2.setBloodPressureSystolic(130.0);
        data2.setBloodPressureDiastolic(85.0);
        data2.setOxygenLevel(97.0);
        data2.setBodyTemperature(36.8);
        repository.save(data2);

        // 生成图表
        byte[] chart = chartService.generateChart("heartRate");

        // 验证图表是否生成
        assertThat(chart).isNotEmpty();
    }
}
