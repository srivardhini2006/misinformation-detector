package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PropagandaDetectorService {

    // ── Propaganda Pattern Dictionary ──────────────
    private static final Map<String, 
        List<String>> PROPAGANDA_PATTERNS = 
        new HashMap<>();

    static {
        PROPAGANDA_PATTERNS.put("fear_appeal",
            Arrays.asList(
                "danger", "threat", "terrifying",
                "catastrophic", "disaster", "attack",
                "destroy", "collapse", "chaos",
                "panic", "alarming", "devastating",
                "crisis", "emergency", "horrifying"
            ));

        PROPAGANDA_PATTERNS.put("loaded_language",
            Arrays.asList(
                "radical", "extremist", "traitor",
                "corrupt", "evil", "wicked",
                "monster", "criminal", "terrorist",
                "enemy", "scum", "vile",
                "disgusting", "despicable", "coward"
            ));

        PROPAGANDA_PATTERNS.put("false_urgency",
            Arrays.asList(
                "urgent", "immediately", "right now",
                "act now", "time is running out",
                "last chance", "before its too late",
                "dont wait", "breaking", "must act",
                "critical moment"
            ));

        PROPAGANDA_PATTERNS.put("bandwagon",
            Arrays.asList(
                "everyone knows", "everybody agrees",
                "all people believe", "the whole world",
                "no one disagrees", "common sense",
                "people are saying", "most people",
                "they say", "obvious to everyone"
            ));

        PROPAGANDA_PATTERNS.put("name_calling",
            Arrays.asList(
                "idiot", "stupid", "moron", "liar",
                "fake", "fraud", "puppet", "clown",
                "hypocrite", "loser", "failure",
                "incompetent", "weak", "coward"
            ));

        PROPAGANDA_PATTERNS.put("exaggeration",
            Arrays.asList(
                "best ever", "worst ever",
                "absolutely", "totally",
                "completely", "never before",
                "unprecedented", "unbelievable",
                "impossible", "perfect", "flawless",
                "guaranteed", "100 percent"
            ));

        PROPAGANDA_PATTERNS.put(
            "conspiracy_indicators",
            Arrays.asList(
                "they dont want you to know",
                "hidden agenda", "cover up",
                "wake up", "mainstream media lies",
                "the truth is", "secret plan",
                "deep state", "controlled by",
                "follow the money", "suppressed",
                "what they hide"
            ));
    }

    // ── Main Detection Method ───────────────────────
    public List<String> detectPropaganda(
            String text) {

        List<String> detectedFlags = 
            new ArrayList<>();

        if (text == null || text.trim().isEmpty()){
            return detectedFlags;
        }

        String lowerText = text.toLowerCase();

        // Check each propaganda category
        for (Map.Entry<String, List<String>> 
                entry : PROPAGANDA_PATTERNS
                .entrySet()) {

            String technique = entry.getKey();
            List<String> keywords = entry.getValue();

            // Check if any keyword exists in text
            for (String keyword : keywords) {
                if (lowerText.contains(keyword)) {
                    detectedFlags.add(technique);
                    break; // one match per category
                }
            }
        }

        return detectedFlags;
    }

    // ── Severity Calculator ─────────────────────────
    public String getSeverity(int flagCount) {
        if (flagCount >= 4) return "HIGH";
        if (flagCount >= 2) return "MEDIUM";
        if (flagCount >= 1) return "LOW";
        return "NONE";
    }

    // ── Convert flags list to String for DB ─────────
    public String flagsToString(
            List<String> flags) {
        if (flags == null || flags.isEmpty()) {
            return "NONE";
        }
        return String.join(", ", flags);
    }
}
