package com.misinfo.misinformationdetector.repository;

import com.misinfo.misinformationdetector.model.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalysisResultRepository 
    extends JpaRepository<AnalysisResult, Long> {
    
    List<AnalysisResult> findByUserId(Long userId);
    List<AnalysisResult> findByClassification(String classification);
}