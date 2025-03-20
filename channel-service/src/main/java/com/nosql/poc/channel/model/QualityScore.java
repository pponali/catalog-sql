package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents quality and ranking information for a product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityScore {
    
    /**
     * Overall quality score (0-100).
     */
    private Integer score;
    
    /**
     * Ranking within its category.
     */
    private Integer categoryRank;
    
    /**
     * Ranking among all products.
     */
    private Integer globalRank;
    
    /**
     * Component scores for different aspects (content, images, etc).
     */
    private Map<String, Integer> componentScores;
    
    /**
     * Last time the score was calculated.
     */
    private String lastUpdated;
    
    /**
     * Whether the score is considered high quality.
     * 
     * @return true if the score is 80 or higher
     */
    public boolean isHighQuality() {
        return score != null && score >= 80;
    }
    
    /**
     * Whether the score is considered medium quality.
     * 
     * @return true if the score is between 50 and 79
     */
    public boolean isMediumQuality() {
        return score != null && score >= 50 && score < 80;
    }
    
    /**
     * Whether the score is considered low quality.
     * 
     * @return true if the score is below 50
     */
    public boolean isLowQuality() {
        return score != null && score < 50;
    }
}