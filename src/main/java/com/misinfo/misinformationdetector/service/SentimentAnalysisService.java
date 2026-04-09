package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SentimentAnalysisService {

    // ── Positive Word Dictionary ────────────────────
    private static final Set<String> POSITIVE_WORDS = 
        new HashSet<>(Arrays.asList(
            "good", "great", "excellent", "amazing",
            "wonderful", "fantastic", "outstanding",
            "positive", "success", "successful",
            "benefit", "beneficial", "effective",
            "proven", "confirmed", "verified",
            "accurate", "correct", "true", "real",
            "genuine", "authentic", "legitimate",
            "credible", "reliable", "trustworthy",
            "honest", "transparent", "fair",
            "balanced", "objective", "reasonable",
            "scientific", "evidence", "research",
            "study", "data", "facts", "truth",
            "improve", "improved", "progress"
        ));

    // ── Negative Word Dictionary ────────────────────
    private static final Set<String> NEGATIVE_WORDS = 
        new HashSet<>(Arrays.asList(
            "bad", "terrible", "awful", "horrible",
            "disgusting", "evil", "corrupt",
            "dangerous", "deadly", "toxic", "harmful",
            "destroy", "fake", "false", "lie",
            "liar", "fraud", "scam", "conspiracy",
            "manipulate", "propaganda", "fear",
            "panic", "emergency", "crisis",
            "catastrophe", "disaster", "death",
            "kill", "murder", "violence", "attack",
            "threat", "shocking", "unbelievable",
            "outrageous", "impossible", "guaranteed"
        ));

    // ── Intensity Amplifiers ────────────────────────
    private static final Set<String> AMPLIFIERS = 
        new HashSet<>(Arrays.asList(
            "extremely", "absolutely", "totally",
            "completely", "utterly", "massively",
            "incredibly", "unbelievably", "critical",
            "urgent", "immediately", "breaking"
        ));

    // ── Negation Words ──────────────────────────────
    private static final Set<String> NEGATIONS = 
        new HashSet<>(Arrays.asList(
            "not", "no", "never", "neither",
            "nor", "nothing", "nowhere", "none"
        ));

    // ── Main Method called by Orchestrator ─────────
    public String analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "NEUTRAL";
        }

        String[] words = text.toLowerCase()
                             .split("\\s+");
        int positiveScore = 0;
        int negativeScore = 0;
        double intensityBonus = 0.0;
        boolean negationContext = false;

        for (String word : words) {
            // Clean punctuation from word
            String clean = word.replaceAll(
                "[^a-z]", "");

            if (NEGATIONS.contains(clean)) {
                negationContext = true;
                continue;
            }

            if (AMPLIFIERS.contains(clean)) {
                intensityBonus += 0.5;
            }

            if (POSITIVE_WORDS.contains(clean)) {
                if (negationContext) {
                    negativeScore += 2;
                } else {
                    positiveScore += 1;
                }
                negationContext = false;

            } else if (NEGATIVE_WORDS.contains(clean)) {
                if (negationContext) {
                    positiveScore += 1;
                } else {
                    negativeScore += 2;
                }
                negationContext = false;

            } else {
                negationContext = false;
            }
        }

        // Apply intensity bonus to negative score
        negativeScore += (int) intensityBonus;

        // Determine final sentiment
        if (positiveScore == 0 && negativeScore == 0){
            return "NEUTRAL";
        } else if (negativeScore > positiveScore) {
            return "NEGATIVE";
        } else if (positiveScore > negativeScore) {
            return "POSITIVE";
        } else {
            return "NEUTRAL";
        }
    }

    // ── Confidence Score (0.0 to 1.0) ──────────────
    public double getSentimentConfidence(
            String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.5;
        }

        String[] words = text.toLowerCase()
                             .split("\\s+");
        int matches = 0;

        for (String word : words) {
            String clean = word.replaceAll(
                "[^a-z]", "");
            if (POSITIVE_WORDS.contains(clean) || 
                NEGATIVE_WORDS.contains(clean)) {
                matches++;
            }
        }

        double ratio = (double) matches / 
                       Math.max(1, words.length);
        return Math.min(1.0, 0.5 + ratio);
    }
}
