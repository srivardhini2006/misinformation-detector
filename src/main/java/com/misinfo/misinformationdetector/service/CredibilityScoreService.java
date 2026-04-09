package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CredibilityScoreService {

    // ── High severity propaganda techniques ────────
    private static final List<String> HIGH_SEVERITY = 
        Arrays.asList(
            "conspiracy_indicators",
            "false_urgency",
            "fear_appeal"
        );

    // ── Main Score Calculator ───────────────────────
    // Called by AnalysisOrchestratorService
    public double calculateScore(
            String sentiment,
            List<String> flags,
            int wordCount) {

        double score = 100.0;

        // Deduction 1: Propaganda flag penalties
        for (String flag : flags) {
            if (HIGH_SEVERITY.contains(flag)) {
                score -= 15.0; // high severity
            } else {
                score -= 10.0; // standard severity
            }
        }

        // Deduction 2: Negative sentiment penalty
        if ("NEGATIVE".equals(sentiment)) {
            score -= 10.0;
        } else if ("POSITIVE".equals(sentiment)) {
            score += 2.0; // slight bonus
        }

        // Deduction 3: Very short text penalty
        if (wordCount < 20) {
            score -= 5.0;
        }

        // Bonus: Long clean text
        if (wordCount > 200 && flags.isEmpty()) {
            score = Math.min(100.0, score + 5.0);
        }

        // Clamp between 0 and 100
        return Math.max(0.0, 
               Math.min(100.0, score));
    }

    // ── Classification ──────────────────────────────
    public String classify(double score) {
        if (score >= 80.0) return "CREDIBLE";
        if (score >= 60.0) return "SUSPICIOUS";
        if (score >= 40.0) return "MISLEADING";
        return "PROPAGANDA";
    }

    // ── Human-readable Summary ──────────────────────
    public String generateSummary(
            double score,
            String classification,
            List<String> flags) {

        StringBuilder sb = new StringBuilder();

        sb.append("Credibility Score: ")
          .append(String.format("%.1f", score))
          .append("/100. ");

        switch (classification) {
            case "CREDIBLE":
                sb.append("This content appears " +
                    "credible. No significant " +
                    "manipulation detected.");
                break;
            case "SUSPICIOUS":
                sb.append("This content shows " +
                    "suspicious patterns. " +
                    "Verify independently.");
                break;
            case "MISLEADING":
                sb.append("This content likely " +
                    "contains distorted " +
                    "information. Be cautious.");
                break;
            case "PROPAGANDA":
                sb.append("Strong propaganda " +
                    "indicators detected. " +
                    "Do not share without " +
                    "verification.");
                break;
        }

        if (!flags.isEmpty()) {
            sb.append(" Detected techniques: ")
              .append(String.join(", ", flags))
              .append(".");
        }

        return sb.toString();
    }

    // ── Color for UI display ────────────────────────
    public String getClassificationColor(
            String classification) {
        switch (classification) {
            case "CREDIBLE":   return "#10b981";
            case "SUSPICIOUS": return "#f59e0b";
            case "MISLEADING": return "#f97316";
            case "PROPAGANDA": return "#ef4444";
            default:           return "#6366f1";
        }
    }
}
