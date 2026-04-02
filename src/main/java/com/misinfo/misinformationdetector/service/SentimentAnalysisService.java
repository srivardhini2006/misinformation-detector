package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class SentimentAnalysisService {

    // ── Positive and Negative Word Lists ───────────
    private static final List<String> 
        POSITIVE_WORDS = Arrays.asList(
            "good", "great", "excellent", "amazing",
            "wonderful", "fantastic", "positive",
            "success", "benefit", "improve",
            "safe", "effective", "proven", "trust",
            "reliable", "accurate", "helpful",
            "support", "progress", "advance"
        );

    private static final List<String> 
        NEGATIVE_WORDS = Arrays.asList(
            "bad", "terrible", "horrible", "awful",
            "dangerous", "deadly", "harmful", "toxic",
            "corrupt", "evil", "destroy", "failure",
            "threat", "fear", "panic", "crisis",
            "disaster", "catastrophe", "warning",
            "urgent", "attack", "lie", "fake",
            "fraud", "manipulation", "deceptive"
        );

    // ── Main Sentiment Analysis Method ─────────────
    public String analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()){
            return "NEUTRAL";
        }

        String lowerText = text.toLowerCase();
        int positiveScore = 0;
        int negativeScore = 0;

        // Count positive word matches
        for (String word : POSITIVE_WORDS) {
            if (lowerText.contains(word)) {
                positiveScore++;
            }
        }

        // Count negative word matches
        for (String word : NEGATIVE_WORDS) {
            if (lowerText.contains(word)) {
                negativeScore++;
            }
        }

        // Determine sentiment
        if (positiveScore > negativeScore) {
            return "POSITIVE";
        } else if (negativeScore > positiveScore){
            return "NEGATIVE";
        } else {
            return "NEUTRAL";
        }
    }

    // ── Sentiment Score (0.0 to 1.0) ───────────────
    public double getSentimentScore(String text) {
        if (text == null || text.trim().isEmpty()){
            return 0.5;
        }

        String lowerText = text.toLowerCase();
        int positiveScore = 0;
        int negativeScore = 0;

        for (String word : POSITIVE_WORDS) {
            if (lowerText.contains(word)) {
                positiveScore++;
            }
        }
        for (String word : NEGATIVE_WORDS) {
            if (lowerText.contains(word)) {
                negativeScore++;
            }
        }

        int total = positiveScore + negativeScore;
        if (total == 0) return 0.5;

        return (double) positiveScore / total;
    }
}
