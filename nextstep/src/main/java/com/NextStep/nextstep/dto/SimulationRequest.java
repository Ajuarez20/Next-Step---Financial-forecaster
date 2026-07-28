package com.NextStep.nextstep.dto;

public class SimulationRequest {

    private String goalType;

    private Double targetAmount;

    private Double upfrontCost;

    private Double monthlyPayment;

    private Integer termMonths;



    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }


    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }


    public Double getUpfrontCost() {
        return upfrontCost;
    }

    public void setUpfrontCost(Double upfrontCost) {
        this.upfrontCost = upfrontCost;
    }


    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }


    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }
}