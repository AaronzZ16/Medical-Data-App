package com.medicaldataapp.service;

import com.medicaldataapp.entity.TimeSeriesData;
import com.medicaldataapp.repository.TimeSeriesDataRepository;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private TimeSeriesDataRepository repository;

    public byte[] generateChart(String column) throws IOException {
        List<TimeSeriesData> dataList = repository.findAll();

        // 按时间戳排序数据
        dataList.sort((d1, d2) -> d1.getTimestamp().compareTo(d2.getTimestamp()));

        XYChart chart = new XYChart(600, 400);
        chart.setTitle(column + " Chart");
        chart.setXAxisTitle("Time");
        chart.setYAxisTitle(column);

        List<Long> xData = dataList.stream()
                .map(data -> data.getTimestamp().getTime())
                .collect(Collectors.toList());

        List<Double> yData;
        switch (column) {
            case "heartRate":
                yData = dataList.stream().map(TimeSeriesData::getHeartRate).collect(Collectors.toList());
                break;
            case "bloodPressureSystolic":
                yData = dataList.stream().map(TimeSeriesData::getBloodPressureSystolic).collect(Collectors.toList());
                break;
            case "bloodPressureDiastolic":
                yData = dataList.stream().map(TimeSeriesData::getBloodPressureDiastolic).collect(Collectors.toList());
                break;
            case "oxygenLevel":
                yData = dataList.stream().map(TimeSeriesData::getOxygenLevel).collect(Collectors.toList());
                break;
            case "bodyTemperature":
                yData = dataList.stream().map(TimeSeriesData::getBodyTemperature).collect(Collectors.toList());
                break;
            default:
                throw new IllegalArgumentException("Invalid column: " + column);
        }

        XYSeries series = chart.addSeries(column, xData, yData);
        series.setMarker(SeriesMarkers.NONE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);

        return baos.toByteArray();
    }
}
