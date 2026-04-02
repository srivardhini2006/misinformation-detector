package com.misinfo.misinformationdetector.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_results")
@Data
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String inputText;

    private Double credibilityScore;

    private String classification;

    private String sentiment;

    @Column(columnDefinition = "TEXT")
    private String propagandaFlags;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt = LocalDateTime.now();
}