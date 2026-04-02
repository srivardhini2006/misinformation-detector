package com.misinfo.misinformationdetector.controller;

import com.misinfo.misinformationdetector.dto.AnalysisRequest;
import com.misinfo.misinformationdetector.dto.AnalysisResponse;
import com.misinfo.misinformationdetector.model.AnalysisResult;
import com.misinfo.misinformationdetector.repository.AnalysisResultRepository;
import com.misinfo.misinformationdetector.service.AnalysisOrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@SuppressWarnings("null")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnalysisController {

    @Autowired
    private AnalysisOrchestratorService orchestrator;

    @Autowired
    private AnalysisResultRepository repository;

    // ── Analyze Text ────────────────────────────────
    @PostMapping("/analyze")
    public AnalysisResponse analyze(
            @RequestBody AnalysisRequest request) {
        return orchestrator.analyze(request);
    }

    // ── Get All Results (Admin) ─────────────────────
    @GetMapping("/history")
    public List<AnalysisResult> getAllResults() {
        return repository.findAll();
    }

    // ── Get Result by ID ────────────────────────────
    @GetMapping("/history/{id}")
    public AnalysisResult getById(
            @PathVariable Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    // ── Delete Result by ID ─────────────────────────
    @DeleteMapping("/history/{id}")
    public String deleteById(
            @PathVariable Long id) {
        repository.deleteById(id);
        return "Record deleted successfully!";
    }

    // ── Health Check ────────────────────────────────
    @GetMapping("/hello")
    public String hello() {
        return "Misinformation Detector is running!";
    }
}