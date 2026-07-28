package com.NextStep.nextstep.dto;

public class SimulationResponse {

    private String status;

    private Double projectedSavings;

    private Double projectedRemainingIncome;
    
    private Double currentFinancialScore;

	private Double projectedFinancialScore;
    
    private String recommendation;
    
    

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getProjectedSavings() {
		return projectedSavings;
	}

	public void setProjectedSavings(Double projectedSavings) {
		this.projectedSavings = projectedSavings;
	}

	public Double getProjectedRemainingIncome() {
		return projectedRemainingIncome;
	}

	public void setProjectedRemainingIncome(Double projectedRemainingIncome) {
		this.projectedRemainingIncome = projectedRemainingIncome;
	}

	public Double getProjectedFinancialScore() {
		return projectedFinancialScore;
	}

	public void setProjectedFinancialScore(Double projectedFinancialScore) {
		this.projectedFinancialScore = projectedFinancialScore;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}


    public Double getCurrentFinancialScore() {
		return currentFinancialScore;
	}

	public void setCurrentFinancialScore(Double currentFinancialScore) {
		this.currentFinancialScore = currentFinancialScore;
	}

	

}