package com.example.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SentimentAnalysisService {

    private static final Map<String, Integer> SENTIMENT_MAP = new HashMap<>();

    static {
        // Positive words
        SENTIMENT_MAP.put("good", 2);
        SENTIMENT_MAP.put("great", 3);
        SENTIMENT_MAP.put("excellent", 4);
        SENTIMENT_MAP.put("happy", 2);
        SENTIMENT_MAP.put("success", 3);

        // Negative words
        SENTIMENT_MAP.put("bad", -2);
        SENTIMENT_MAP.put("terrible", -4);
        SENTIMENT_MAP.put("worst", -3);
        SENTIMENT_MAP.put("sad", -2);
        SENTIMENT_MAP.put("danger", -3);
        SENTIMENT_MAP.put("fear", -3);
    }

    private static final Set<String> NEGATIONS = new HashSet<>(
            Arrays.asList("not", "no", "never")
    );

    public Map<String, Object> analyzeSentiment(String text) {

        String[] words = text.toLowerCase().split("\\s+");

        int score = 0;

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            if (SENTIMENT_MAP.containsKey(word)) {

                int value = SENTIMENT_MAP.get(word);

                // Check for negation before word
                if (i > 0 && NEGATIONS.contains(words[i - 1])) {
                    value = -value;
                }

                score += value;
            }
        }

        String sentiment;
        if (score > 1) sentiment = "POSITIVE";
        else if (score < -1) sentiment = "NEGATIVE";
        else sentiment = "NEUTRAL";

        double confidence = Math.abs(score) / (double) words.length;

        Map<String, Object> result = new HashMap<>();
        result.put("sentiment", sentiment);
        result.put("confidence", confidence);

        return result;
    }
}
