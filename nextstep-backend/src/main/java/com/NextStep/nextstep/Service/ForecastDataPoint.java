package com.NextStep.nextstep.Service;

public class ForecastDataPoint {
    private String monthLabel;
    private double projectedSavings;
    private double goalTarget;

    public ForecastDataPoint(String monthLabel, double projectedSavings, double goalTarget) {
        this.monthLabel = monthLabel;
        this.projectedSavings = projectedSavings;
        this.goalTarget = goalTarget;
    }

    public String getMonthLabel() { return monthLabel; }
    public double getProjectedSavings() { return projectedSavings; }
    public double getGoalTarget() { return goalTarget; }
}