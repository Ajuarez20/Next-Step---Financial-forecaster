package com.NextStep.nextstep.controller;

import com.NextStep.nextstep.Service.ForecastDataPoint;
import com.NextStep.nextstep.Service.ForecastRequest;
import com.NextStep.nextstep.Service.ForecastService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ForecastController {

    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @PostMapping("/api/forecast")
    public List calculateForecast(@RequestBody ForecastRequest request) {
        int months = request.getProjectionMonths() > 0 ? request.getProjectionMonths() : 12;

        return forecastService.generateProjection(
            request.getMonthlyIncome(),
            request.getMonthlyExpenses(),
            request.getCurrentSavings(),
            request.getTargetGoalAmount(),
            months
        );
    }
}