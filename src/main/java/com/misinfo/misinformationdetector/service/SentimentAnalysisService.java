package com.veritas.detector.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SentimentAnalysisService {

    private static final Set<String> POSITIVE_WORDS = Set.of(
        "good", "great", "excellent", "amazing", "wonderful", "fantastic", "outstanding",
        "positive", "success", "successful", "benefit", "beneficial", "effective",
        "proven", "confirmed", "verified", "accurate", "correct", "true", "real",
        "genuine", "authentic", "legitimate", "credible", "reliable", "trustworthy",
        "honest", "transparent", "fair", "balanced", "objective", "reasonable",
        "scientific", "evidence", "research", "study", "data", "facts", "truth",
        "improve", "improved", "improving", "progress", "growth", "increase"
    );

    private static final Set<String> NEGATIVE_WORDS = Set.of(
        "bad", "terrible", "awful", "horrible", "disgusting", "evil", "corrupt",
        "dangerous", "deadly", "toxic", "harmful", "destroy", "destroying", "destroyed",
        "fake", "false", "lie", "lying", "liar", "fraud", "fraudulent", "scam",
        "conspiracy", "cover-up", "coverup", "manipulate", "manipulation", "propaganda",
        "brainwash", "fear", "panic", "emergency", "crisis", "catastrophe", "disaster",
        "death", "kill", "murder", "violence", "attack", "threat", "threatening",
        "secret", "hidden", "exposed", "shocking", "unbelievable", "outrageous",
        "never", "always", "everyone", "nobody", "impossible", "guaranteed"
    );

    private static final Set<String> INTENSITY_AMPLIFIERS = Set.of(
        "extremely", "absolutely", "totally", "completely", "utterly", "massively",
        "incredibly", "unbelievably", "shocking", "devastating", "explosive",
        "critical", "urgent", "immediately", "now", "breaking", "exclusive", "bombshell"
    );

    private static final Set<String> NEGATIONS = Set.of(
        "not", "no", "never", "don't", "doesn't", "didn't", "won't", "can't",
        "cannot", "neither", "nor", "nothing", "nowhere", "nobody", "none"
    );

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    public record SentimentResult(Sentiment sentiment, double confidence, double intensity) {}

    public SentimentResult analyze(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return new SentimentResult(Sentiment.NEUTRAL, 0.5, 0.0);
        }

        int positiveScore = 0;
        int negativeScore = 0;
        double intensityScore = 0.0;
        boolean negationContext = false;

        for (int i = 0; i < tokens.size(); i++) {
            String word = tokens.get(i);

            if (NEGATIONS.contains(word)) {
                negationContext = true;
                continue;
            }

            if (INTENSITY_AMPLIFIERS.contains(word)) {
                intensityScore += 1.0;
            }

            if (POSITIVE_WORDS.contains(word)) {
                if (negationContext) {
                    negativeScore += 2;
                } else {
                    positiveScore += 1;
                }
                negationContext = false;
            } else if (NEGATIVE_WORDS.contains(word)) {
                if (negationContext) {
                    positiveScore += 1;
                } else {
                    negativeScore += 2;
                }
                negationContext = false;
            } else if (negationContext) {
                negationContext = false;
            }
        }

        int totalTokens = tokens.size();
        double normalizedIntensity = Math.min(1.0, intensityScore / Math.max(1, totalTokens * 0.05));

        Sentiment sentiment;
        double confidence;
        int totalSentimentWords = positiveScore + negativeScore;

        if (totalSentimentWords == 0) {
            sentiment = Sentiment.NEUTRAL;
            confidence = 0.6;
        } else {
            double positiveRatio = (double) positiveScore / totalSentimentWords;
            double negativeRatio = (double) negativeScore / totalSentimentWords;

            if (positiveRatio > 0.6) {
                sentiment = Sentiment.POSITIVE;
                confidence = 0.5 + (positiveRatio * 0.5);
            } else if (negativeRatio > 0.6) {
                sentiment = Sentiment.NEGATIVE;
                confidence = 0.5 + (negativeRatio * 0.5);
            } else {
                sentiment = Sentiment.NEUTRAL;
                confidence = 0.5;
            }
        }

        confidence = Math.min(1.0, Math.max(0.0, confidence));
        return new SentimentResult(sentiment, confidence, normalizedIntensity);
    }
}
