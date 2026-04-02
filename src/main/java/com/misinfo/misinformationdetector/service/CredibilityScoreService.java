package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CredibilityScoreService {

    // ── Main Score Calculator ───────────────────────
    public double calculateScore(
            String sentiment,
            List<String> propagandaFlags,
            int wordCount) {

        double score = 100.0;

        // ── Deduct for propaganda flags ─────────────
        int flagCount = propagandaFlags.size();
        score -= flagCount * 15.0;

        // ── Deduct for negative sentiment ───────────
        if (sentiment.equals("NEGATIVE")) {
            score -= 15.0;
        } else if (sentiment.equals("NEUTRAL")) {
            score -= 5.0;
        }

        // ── Deduct for very short text ──────────────
        if (wordCount < 10) {
            score -= 10.0;
        }

        // ── Keep score between 0 and 100 ────────────
        if (score < 0) score = 0;
        if (score > 100) score = 100;

        return Math.round(score * 100.0) 
                / 100.0;
    }

    // ── Classification Label ────────────────────────
    public String classify(double score) {
        if (score >= 75) return "CREDIBLE";
        if (score >= 60) return "LIKELY_CREDIBLE";
        if (score >= 40) return "SUSPICIOUS";
        if (score >= 20) return "MISLEADING";
        return "PROPAGANDA";
    }

    // ── Plain English Summary ───────────────────────
    public String generateSummary(
            double score,
            String classification,
            List<String> flags) {

        StringBuilder summary = 
            new StringBuilder();

        summary.append("Credibility Score: ")
               .append(score)
               .append("/100. ");

        summary.append("Classification: ")
               .append(classification)
               .append(". ");

        if (!flags.isEmpty()) {
            summary.append(
                "Detected manipulation techniques: ")
                   .append(
                String.join(", ", flags))
                   .append(". ");
        } else {
            summary.append(
                "No propaganda techniques detected. ");
        }

        if (classification.equals("PROPAGANDA") 
            || classification.equals("MISLEADING")){
            summary.append(
                "This content shows strong " +
                "indicators of misinformation.");
        } else if (classification
                .equals("SUSPICIOUS")) {
            summary.append(
                "Verify this content from " +
                "trusted sources.");
        } else {
            summary.append(
                "This content appears " +
                "relatively credible.");
        }

        return summary.toString();
    }
}
 
 
