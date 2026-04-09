package com.veritas.detector.service;

import com.veritas.detector.service.SentimentAnalysisService.SentimentResult;
import com.veritas.detector.service.PropagandaDetectorService.PropagandaResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredibilityScoreService {

    public enum Classification {
        CREDIBLE, SUSPICIOUS, MISLEADING, PROPAGANDA
    }

    public record CredibilityResult(
        double score,
        Classification classification,
        String classificationColor,
        String summary,
        boolean isSafe
    ) {}

    private static final List<String> HIGH_SEVERITY_FLAGS = List.of(
        "Conspiracy Indicators",
        "False Urgency",
        "Emotional Manipulation",
        "Black-and-White Thinking"
    );

    public CredibilityResult calculate(
            SentimentResult sentimentResult,
            PropagandaResult propagandaResult,
            int wordCount,
            int sentenceCount) {

        double score = 100.0;

        // Deduction 1: Propaganda flags
        for (String flag : propagandaResult.flags()) {
            if (HIGH_SEVERITY_FLAGS.contains(flag)) {
                score -= 12.0;
            } else {
                score -= 8.0;
            }
        }

        // Deduction 2: Negative sentiment penalty
        if (sentimentResult.sentiment() == SentimentAnalysisService.Sentiment.NEGATIVE) {
            double sentimentPenalty = 10.0 * sentimentResult.confidence();
            score -= sentimentPenalty;
        }

        // Deduction 3: Emotional intensity penalty
        double intensityPenalty = sentimentResult.intensity() * 10.0;
        score -= intensityPenalty;

        // Penalty for very short texts
        if (wordCount < 20) {
            score -= 5.0;
        }

        // Bonus for long clean texts
        if (wordCount > 200 && propagandaResult.flags().isEmpty()) {
            score = Math.min(100.0, score + 3.0);
        }

        // Bonus for good sentence structure
        if (sentenceCount > 0) {
            double wordsPerSentence = (double) wordCount / sentenceCount;
            if (wordsPerSentence >= 10 && wordsPerSentence <= 30) {
                score = Math.min(100.0, score + 2.0);
            }
        }

        // Clamp to 0-100
        score = Math.max(0.0, Math.min(100.0, score));
        score = Math.round(score * 10.0) / 10.0;

        // Classification
        Classification classification;
        String color;
        boolean isSafe;

        if (score >= 80.0) {
            classification = Classification.CREDIBLE;
            color = "#10b981";
            isSafe = true;
        } else if (score >= 60.0) {
            classification = Classification.SUSPICIOUS;
            color = "#f59e0b";
            isSafe = false;
        } else if (score >= 40.0) {
            classification = Classification.MISLEADING;
            color = "#f97316";
            isSafe = false;
        } else {
            classification = Classification.PROPAGANDA;
            color = "#ef4444";
            isSafe = false;
        }

        String summary = buildSummary(score, classification,
                propagandaResult.flags(), sentimentResult.sentiment(), wordCount);

        return new CredibilityResult(score, classification, color, summary, isSafe);
    }

    private String buildSummary(double score, Classification classification,
                                 List<String> flags,
                                 SentimentAnalysisService.Sentiment sentiment,
                                 int wordCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Veritas.AI assigned a credibility score of ")
          .append(String.format("%.1f", score))
          .append("/100. ");

        switch (classification) {
            case CREDIBLE ->
                sb.append("This content appears credible with no significant manipulation indicators detected.");
            case SUSPICIOUS ->
                sb.append("This content shows some suspicious characteristics. Exercise caution and verify claims independently.");
            case MISLEADING ->
                sb.append("This content exhibits multiple misleading patterns. It likely contains distorted information.");
            case PROPAGANDA ->
                sb.append("This content shows strong signs of propaganda or deliberate misinformation. Do not share without verification.");
        }

        if (!flags.isEmpty()) {
            sb.append(" Detected techniques: ").append(String.join(", ", flags)).append(".");
        }

        sb.append(" Sentiment: ").append(sentiment.name().toLowerCase()).append(".");
        sb.append(" Analyzed ").append(wordCount).append(" words.");

        return sb.toString();
    }
}
