package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CredibilityScoreService {

    // ── Flag Weights (Balanced) ─────────────────────
    private static final Map<String, Double> FLAG_WEIGHTS = Map.of(
        "unverified_death_claim", 25.0,
        "false_urgency", 10.0,
        "extreme_claim", 20.0,
        "sensitive_claim", 15.0,
        "fear_appeal", 12.0,
        "conspiracy_indicators", 15.0
    );

    // ── Main Score Calculator ───────────────────────
    public double calculateScore(
            String text,
            String sentiment,
            List<String> flags,
            int wordCount) {

        double score = 100.0;
        String lower = text.toLowerCase();

        // Use Set to avoid duplicates
        Set<String> detectedFlags = new HashSet<>();

        // ── Detection Phase ─────────────────────────

        // Death claim (word boundary match)
        if (lower.matches(".\\b(dead|died|killed)\\b.")) {
            detectedFlags.add("unverified_death_claim");
        }

        // Urgency / clickbait
        if (lower.matches(".\\b(breaking|urgent|shocking|alert)\\b.")) {
            detectedFlags.add("false_urgency");
        }

        // Extreme claims
        if (lower.matches(".\\b(war|attack|explosion|massacre)\\b.")) {
            detectedFlags.add("extreme_claim");
        }

        // Political sensitive + extreme combo
        if (lower.matches(".\\b(trump|modi|biden|china|india)\\b.") &&
            lower.matches(".\\b(war|dead|attack|killed)\\b.")) {
            detectedFlags.add("sensitive_claim");
        }

        // Fear-based language
        if (lower.matches(".\\b(danger|threat|panic|fear)\\b.")) {
            detectedFlags.add("fear_appeal");
        }

        // Conspiracy indicators
        if (lower.matches(".\\b(secret|hidden|they don.?t want you to know|exposed)\\b.")) {
            detectedFlags.add("conspiracy_indicators");
        }

        // ── Scoring Phase ───────────────────────────

        for (String flag : detectedFlags) {
            score -= FLAG_WEIGHTS.getOrDefault(flag, 8.0);
        }

        // Copy detected flags back to original list
        flags.clear();
        flags.addAll(detectedFlags);

        // ── Sentiment Adjustment ────────────────────

        if ("NEGATIVE".equalsIgnoreCase(sentiment)) {
            score -= 5.0;
        } else if ("POSITIVE".equalsIgnoreCase(sentiment)) {
            score += 2.0;
        }
        // NEUTRAL → no change

        // ── Length Adjustment ───────────────────────

        if (wordCount < 20) {
            score -= 5.0;
        } else if (wordCount > 150 && detectedFlags.isEmpty()) {
            score += 5.0;
        }

        // ── Clamp Score ─────────────────────────────

        return Math.max(0.0, Math.min(100.0, score));
    }

    // ── Classification ─────────────────────────────
    public String classify(double score) {
        if (score >= 85.0) return "CREDIBLE";
        if (score >= 65.0) return "SUSPICIOUS";
        if (score >= 45.0) return "MISLEADING";
        return "PROPAGANDA";
    }

    // ── Summary ────────────────────────────────────
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
                sb.append("This content appears reliable.");
                break;
            case "SUSPICIOUS":
                sb.append("This content has some suspicious signals.");
                break;
            case "MISLEADING":
                sb.append("This content may mislead readers.");
                break;
            case "PROPAGANDA":
                sb.append("Strong signs of manipulation detected.");
                break;
        }

        if (!flags.isEmpty()) {
            sb.append(" Detected: ")
              .append(String.join(", ", flags));
        }

        return sb.toString();
    }

    // ── UI Color ───────────────────────────────────
    public String getClassificationColor(String classification) {
        switch (classification) {
            case "CREDIBLE":   return "#10b981";
            case "SUSPICIOUS": return "#f59e0b";
            case "MISLEADING": return "#f97316";
            case "PROPAGANDA": return "#ef4444";
            default:           return "#6366f1";
        }
    }
}