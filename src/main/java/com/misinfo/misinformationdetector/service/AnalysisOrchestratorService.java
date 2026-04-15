package com.misinfo.misinformationdetector.service;

import com.misinfo.misinformationdetector.dto.AnalysisRequest;
import com.misinfo.misinformationdetector.dto.AnalysisResponse;
import com.misinfo.misinformationdetector.model.AnalysisResult;
import com.misinfo.misinformationdetector.repository.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalysisOrchestratorService {

    @Autowired
    private TextPreprocessorService preprocessor;

    @Autowired
    private SentimentAnalysisService sentimentService;

    @Autowired
    private PropagandaDetectorService propagandaService;

    @Autowired
    private CredibilityScoreService scoreService;

    @Autowired
    private AnalysisResultRepository repository;

    public AnalysisResponse analyze(
            AnalysisRequest request) {

        String originalText = request.getText();

        // ── Step 1: Preprocess ──────────────────────
        String cleanedText = preprocessor
                .cleanText(originalText);
        int wordCount = preprocessor
                .getWordCount(cleanedText);

        // ── Step 2: Detect Propaganda ───────────────
        List<String> flags = propagandaService
                .detectPropaganda(originalText);

        // ── Step 3: Analyze Sentiment ───────────────
        String sentiment = sentimentService
                .analyzeSentiment(originalText);

        // ── Step 4: Calculate Score ─────────────────
        double score = scoreService
                .calculateScore(
                    cleanedText, sentiment, flags, wordCount);

        // ── Step 5: Classify ────────────────────────
        String classification = scoreService
                .classify(score);

        // ── Step 6: Generate Summary ────────────────
        String summary = scoreService
                .generateSummary(
                    score, classification, flags);

        // ── Step 7: Save to Database ────────────────
        AnalysisResult result = new AnalysisResult();
        result.setInputText(originalText);
        result.setCredibilityScore(score);
        result.setClassification(classification);
        result.setSentiment(sentiment);
        result.setPropagandaFlags(
            propagandaService.flagsToString(flags));
        result.setSummary(summary);
        repository.save(result);

        // ── Step 8: Build Response ──────────────────
        AnalysisResponse response = 
            new AnalysisResponse();
        response.setInputText(originalText);
        response.setCredibilityScore(score);
        response.setClassification(classification);
        response.setSentiment(sentiment);
        response.setPropagandaFlags(flags);
        response.setSummary(summary);

        return response;
    }
}