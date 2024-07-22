package com.medicaldataapp.repository;

import com.medicaldataapp.entity.TimeSeriesData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TimeSeriesDataRepository.class))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TimeSeriesDataRepositoryTests {

    @Autowired
    private TimeSeriesDataRepository repository;

    @Test
    public void testSaveAndFind() {
        TimeSeriesData data = new TimeSeriesData();
        data.setTimestamp(new Timestamp(System.currentTimeMillis()));
        data.setPatientId("1");
        data.setHeartRate(72.0);
        data.setBloodPressureSystolic(120.0);
        data.setBloodPressureDiastolic(80.0);
        data.setOxygenLevel(98.0);
        data.setBodyTemperature(36.5);
        repository.save(data);

        List<TimeSeriesData> result = repository.findAll();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPatientId()).isEqualTo("1");
        assertThat(result.get(0).getHeartRate()).isEqualTo(72.0);
    }
}
