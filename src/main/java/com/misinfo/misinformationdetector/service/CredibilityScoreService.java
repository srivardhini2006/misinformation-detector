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
    public double calculateScore(
            String text,
            String sentiment,
            List<String> flags,
            int wordCount) {

        double score = 100.0;

        // Normalize text
        String lower = text.toLowerCase();

        // 🚨 Detect false death claims
        if (lower.contains("dead") || lower.contains("died")) {
            score -= 40.0;
            flags.add("unverified_death_claim");
        }

        // 🚨 Detect urgency manipulation
        if (lower.contains("breaking") || lower.contains("urgent")) {
            score -= 10.0;
            flags.add("false_urgency");
        }

        // 🚨 Detect war/extreme claims (IMPROVED)
        if (lower.contains("war") || lower.contains("attack")) {
            score -= 50.0;
            flags.add("extreme_claim");
        }

        // 🚨 Detect political sensitive claims
        if (lower.matches(".*(trump|modi|biden|china|india).*")) {
            if (lower.contains("war") || lower.contains("dead") || lower.contains("attack")) {
                score -= 30.0;
                flags.add("sensitive_claim");
            }
        }

        // ── Deduction 1: Propaganda flags ───────────
        for (String flag : flags) {
            if (HIGH_SEVERITY.contains(flag)) {
                score -= 15.0;
            } else {
                score -= 10.0;
            }
        }

        // ── Deduction 2: Sentiment ──────────────────
        if ("NEGATIVE".equals(sentiment)) {
            score -= 10.0;
        } else if ("POSITIVE".equals(sentiment)) {
            score += 2.0;
        }

        // ── Deduction 3: Short text ─────────────────
        if (wordCount < 20) {
            score -= 10.0;
        }

        // ── Bonus: Long clean text ──────────────────
        if (wordCount > 200 && flags.isEmpty()) {
            score = Math.min(100.0, score + 5.0);
        }

        // Clamp score
        return Math.max(0.0,
               Math.min(100.0, score));
    }

    // ── Classification ─────────────────────────────
    public String classify(double score) {
        if (score >= 85.0) return "CREDIBLE";
        if (score >= 60.0) return "SUSPICIOUS";
        if (score >= 40.0) return "MISLEADING";
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
                sb.append("This content appears credible.");
                break;
            case "SUSPICIOUS":
                sb.append("This content shows suspicious patterns.");
                break;
            case "MISLEADING":
                sb.append("This content may be misleading.");
                break;
            case "PROPAGANDA":
                sb.append("Strong propaganda indicators detected.");
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