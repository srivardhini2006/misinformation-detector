package com.misinfo.misinformationdetector.dto;

import lombok.Data;
import java.util.List;

@Data
public class AnalysisResponse {
    private String inputText;
    private Double credibilityScore;
    private String classification;
    private String sentiment;
    private List<String> propagandaFlags;
    private String summary;
}