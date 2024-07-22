package com.medicaldataapp.controller;

import com.medicaldataapp.entity.TimeSeriesData;
import com.medicaldataapp.service.ChartService;
import com.medicaldataapp.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
public class CsvController {

    @Autowired
    private CsvService csvService;

    @Autowired
    private ChartService chartService;

    @GetMapping("/csv/upload")
    public String showUploadForm(Model model) {
        List<TimeSeriesData> dataList = csvService.getAllData();
        model.addAttribute("resultList", dataList);
        return "upload";
    }

    @PostMapping("/csv/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam(value = "columns", required = false) List<String> columns, Model model) {
        try {
            String csvContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<TimeSeriesData> dataList = csvService.processCsv(csvContent);
            model.addAttribute("message", "File processed successfully");
            model.addAttribute("resultList", dataList);
            if (columns != null && !columns.isEmpty()) {
                addChartsToModel(model, columns);
            } else {
                model.addAttribute("message", "File processed successfully but no charts were generated. Please select columns for chart generation.");
            }
        } catch (IOException e) {
            model.addAttribute("message", "Error processing file: " + e.getMessage());
        }
        return "result";
    }

    @PostMapping("/csv/delete/{id}")
    public String deleteData(@PathVariable Long id, Model model) {
        csvService.deleteDataById(id);
        List<TimeSeriesData> dataList = csvService.getAllData();
        model.addAttribute("resultList", dataList);
        model.addAttribute("message", "Data deleted successfully");
        return "result";
    }

    @PostMapping("/csv/deleteAll")
    public String deleteAllData(Model model) {
        csvService.deleteAllData();
        model.addAttribute("resultList", List.of()); // 清空resultList
        model.addAttribute("message", "All data deleted successfully");
        return "result";
    }

    @GetMapping("/csv/edit/{id}")
    public String editDataForm(@PathVariable Long id, Model model) {
        TimeSeriesData data = csvService.getDataById(id);
        model.addAttribute("data", data);
        return "edit";
    }

    @PostMapping("/csv/edit")
    public String editData(@ModelAttribute TimeSeriesData data, Model model) {
        csvService.updateData(data);
        List<TimeSeriesData> dataList = csvService.getAllData();
        model.addAttribute("resultList", dataList);
        return "result";
    }

    @PostMapping("/csv/generateCharts")
    public String generateCharts(@RequestParam("columns") List<String> columns, Model model) {
        List<TimeSeriesData> dataList = csvService.getAllData();
        model.addAttribute("resultList", dataList);
        if (columns != null && !columns.isEmpty()) {
            addChartsToModel(model, columns);
        } else {
            model.addAttribute("message", "No columns selected for chart generation.");
        }
        return "result";
    }

    private void addChartsToModel(Model model, List<String> columns) {
        try {
            for (String column : columns) {
                byte[] chart = chartService.generateChart(column);
                model.addAttribute(column + "Chart", Base64.getEncoder().encodeToString(chart));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
