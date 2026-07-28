package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;
import com.NextStep.nextstep.dto.SimulationRequest;
import com.NextStep.nextstep.dto.SimulationResponse;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.FinancialProfileRepository;

@Service
public class SimulationService {

    private final FinancialProfileRepository financialProfileRepository;
    private final FinancialProfileService financialProfileService;

    public SimulationService(
            FinancialProfileRepository financialProfileRepository,
            FinancialProfileService financialProfileService) {
        this.financialProfileRepository = financialProfileRepository;
        this.financialProfileService = financialProfileService;
    }

    public SimulationResponse simulate(
            Integer financialProfileId,
            SimulationRequest request) {

        FinancialProfile profile = financialProfileRepository.findById(financialProfileId)
                .orElseThrow(() -> new RuntimeException("Financial Profile not found"));

        Double currentRemainingIncome = financialProfileService.calculateRemainingIncome(financialProfileId);
        Double currentScore = financialProfileService.calculateFinancialScore(financialProfileId);

        Double upfrontCost = request.getUpfrontCost() == null ? 0.0 : request.getUpfrontCost();
        Double monthlyPayment = request.getMonthlyPayment() == null ? 0.0 : request.getMonthlyPayment();
        int termMonths = (request.getTermMonths() != null && request.getTermMonths() > 0) ? request.getTermMonths() : 12;

        Double currentSavings = profile.getCurrentSavings() != null ? profile.getCurrentSavings() : 0.0;
        
        Double projectedRemainingIncome = currentRemainingIncome - monthlyPayment;
        Double projectedSavings = (currentSavings - upfrontCost) + (projectedRemainingIncome * termMonths);

        SimulationResponse response = new SimulationResponse();

        if (projectedRemainingIncome < 0) {
            response.setStatus("NOT_RECOMMENDED");
            response.setRecommendation("Do not make this purchase right now. It creates negative monthly cash flow.");
        } else if (currentSavings < upfrontCost) {
            response.setStatus("CAUTION");
            response.setRecommendation("Proceed with caution. This purchase requires more upfront savings than you currently have available.");
        } else {
            response.setStatus("AFFORDABLE");
            response.setRecommendation("Go for it! This goal fits comfortably within your current financial situation.");
        }

        Double projectedScore = financialProfileService.calculateProjectedFinancialScore(
                financialProfileId, 
                projectedSavings, 
                monthlyPayment
        );

        response.setProjectedSavings(projectedSavings);
        response.setProjectedRemainingIncome(projectedRemainingIncome);
        response.setCurrentFinancialScore(currentScore);
        response.setProjectedFinancialScore(projectedScore);

        return response;
    }
}