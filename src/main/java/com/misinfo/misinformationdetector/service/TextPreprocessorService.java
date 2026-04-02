package com.misinfo.misinformationdetector.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextPreprocessorService {

    public String cleanText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // Step 1: Convert to lowercase
        String cleaned = text.toLowerCase();

        // Step 2: Remove URLs
        cleaned = cleaned.replaceAll(
            "http[s]?://\\S+", "");

        // Step 3: Remove special characters
        // keep only letters and spaces
        cleaned = cleaned.replaceAll(
            "[^a-zA-Z\\s]", " ");

        // Step 4: Remove extra whitespace
        cleaned = cleaned.replaceAll(
            "\\s+", " ").trim();

        return cleaned;
    }

    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        // Split text into individual words
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toList());
    }

    public int getWordCount(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.split("\\s+").length;
    }

    public int getSentenceCount(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        // Split by . ! ?
        String[] sentences = text.split(
            "[.!?]+");
        return sentences.length;
    }
}

