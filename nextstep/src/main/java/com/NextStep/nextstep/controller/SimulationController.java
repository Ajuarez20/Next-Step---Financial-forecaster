package com.NextStep.nextstep.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NextStep.nextstep.Service.SimulationService;
import com.NextStep.nextstep.dto.SimulationRequest;
import com.NextStep.nextstep.dto.SimulationResponse;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/{id}")
    public SimulationResponse runSimulation(
            @PathVariable Integer id, 
            @RequestBody SimulationRequest request) {
        // Must match the 'simulate' method name in SimulationService
        return simulationService.simulate(id, request);
    }
}