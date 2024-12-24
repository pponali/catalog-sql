package com.scaler.service.analytics;

import com.scaler.model.FeatureValueAudit;
import com.scaler.model.ValueTrendAnalysis;
import com.scaler.repository.FeatureValueAuditRepository;
import com.scaler.repository.ProductFeatureValueRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FeatureValueAnalytics {

    private final ProductFeatureValueRepository valueRepository;
    private final FeatureValueAuditRepository auditRepository;

    public ValueTrendAnalysis analyzeTrends(Long featureId, String interval, LocalDateTime start, LocalDateTime end) {
        List<FeatureValueAudit> audits = auditRepository.findByFeatureIdAndTimeRangeAndActions(
                featureId, start, end,
                Arrays.asList(FeatureValueAudit.AuditAction.CREATE, FeatureValueAudit.AuditAction.UPDATE)
        );

        Map<String, Double> numericTrends = calculateNumericTrends(audits, interval);
        Map<String, Long> categoryDistribution = calculateCategoryDistribution(audits, interval);
        Map<String, Double> percentageChanges = calculatePercentageChanges(numericTrends);

        DescriptiveStatistics stats = calculateStatistics(numericTrends.values());

        return ValueTrendAnalysis.builder()
                .featureId(featureId)
                .interval(interval)
                .startDate(start)
                .endDate(end)
                .numericTrends(numericTrends)
                .categoryDistribution(categoryDistribution)
                .percentageChanges(percentageChanges)
                .minValue(stats.getMin())
                .maxValue(stats.getMax())
                .avgValue(stats.getMean())
                .standardDeviation(stats.getStandardDeviation())
                .totalCount(audits.size())
                .uniqueValuesCount(countUniqueValues(audits))
                .customMetrics(calculateCustomMetrics(audits))
                .build();
    }

    private Map<String, Double> calculateNumericTrends(List<FeatureValueAudit> audits, String interval) {
        return audits.stream()
                .filter(audit -> audit.getNewValue() != null && audit.getNewValue().get("type").asText().equals("number"))
                .collect(Collectors.groupingBy(
                        audit -> formatDateTime(audit.getTimestamp(), interval),
                        Collectors.averagingDouble(audit -> audit.getNewValue().get("value").asDouble())
                ));
    }

    private Map<String, Long> calculateCategoryDistribution(List<FeatureValueAudit> audits, String interval) {
        return audits.stream()
                .filter(audit -> audit.getNewValue() != null)
                .collect(Collectors.groupingBy(
                        audit -> formatDateTime(audit.getTimestamp(), interval),
                        Collectors.counting()
                ));
    }

    private Map<String, Double> calculatePercentageChanges(Map<String, Double> trends) {
        Map<String, Double> changes = new HashMap<>();
        List<String> dates = new ArrayList<>(trends.keySet());
        Collections.sort(dates);

        for (int i = 1; i < dates.size(); i++) {
            String currentDate = dates.get(i);
            String previousDate = dates.get(i - 1);
            double currentValue = trends.get(currentDate);
            double previousValue = trends.get(previousDate);

            double percentageChange = previousValue == 0 ? 0 :
                    ((currentValue - previousValue) / previousValue) * 100;
            changes.put(currentDate, percentageChange);
        }

        return changes;
    }

    private DescriptiveStatistics calculateStatistics(Collection<Double> values) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        values.forEach(stats::addValue);
        return stats;
    }

    private long countUniqueValues(List<FeatureValueAudit> audits) {
        return audits.stream()
                .map(audit -> audit.getNewValue().toString())
                .distinct()
                .count();
    }

    private Map<String, Object> calculateCustomMetrics(List<FeatureValueAudit> audits) {
        Map<String, Object> metrics = new HashMap<>();

        // Calculate change frequency
        metrics.put("changeFrequency", calculateChangeFrequency(audits));

        // Calculate value stability
        metrics.put("valueStability", calculateValueStability(audits));

        // Calculate type distribution
        metrics.put("typeDistribution", calculateTypeDistribution(audits));

        return metrics;
    }

    private double calculateChangeFrequency(List<FeatureValueAudit> audits) {
        if (audits.isEmpty()) return 0.0;

        LocalDateTime start = audits.get(0).getTimestamp();
        LocalDateTime end = audits.get(audits.size() - 1).getTimestamp();
        long daysBetween = ChronoUnit.DAYS.between(start, end);

        return daysBetween == 0 ? audits.size() : (double) audits.size() / daysBetween;
    }

    private double calculateValueStability(List<FeatureValueAudit> audits) {
        if (audits.size() < 2) return 1.0;

        int changes = 0;
        for (int i = 1; i < audits.size(); i++) {
            if (!audits.get(i).getNewValue().equals(audits.get(i - 1).getNewValue())) {
                changes++;
            }
        }

        return 1.0 - ((double) changes / (audits.size() - 1));
    }

    private Map<String, Long> calculateTypeDistribution(List<FeatureValueAudit> audits) {
        return audits.stream()
                .map(audit -> audit.getNewValue().get("type").asText())
                .collect(Collectors.groupingBy(
                        type -> type,
                        Collectors.counting()
                ));
    }

    private String formatDateTime(LocalDateTime dateTime, String interval) {
        switch (interval.toLowerCase()) {
            case "hourly":
                return dateTime.truncatedTo(ChronoUnit.HOURS).toString();
            case "daily":
                return dateTime.truncatedTo(ChronoUnit.DAYS).toString();
            case "weekly":
                return dateTime.truncatedTo(ChronoUnit.WEEKS).toString();
            case "monthly":
                return dateTime.truncatedTo(ChronoUnit.MONTHS).toString();
            default:
                return dateTime.toString();
        }
    }
}
