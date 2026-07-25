package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ForecastService {

    public List generateProjection(double income, double expenses, double startingSavings, double goal, int months) {
        List trajectory = new ArrayList<>();
        double monthlyNet = income - expenses;
        double accumulatedSavings = startingSavings;

        for (int i = 0; i <= months; i++) {
            trajectory.add(new ForecastDataPoint("Month " + i, Math.round(accumulatedSavings), goal));
            accumulatedSavings += monthlyNet;
        }

        return trajectory;
    }
}