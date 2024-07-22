package com.medicaldataapp.controller;

import com.medicaldataapp.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping("/chart")
    public String getChart(Model model) {
        try {
            byte[] chart = chartService.generateChart("heartRate");
            String base64Chart = java.util.Base64.getEncoder().encodeToString(chart);
            model.addAttribute("chart", base64Chart);
        } catch (Exception e) {
            model.addAttribute("chartError", "Error generating chart: " + e.getMessage());
        }
        return "chart"; // 返回 chart.html 视图
    }
}
