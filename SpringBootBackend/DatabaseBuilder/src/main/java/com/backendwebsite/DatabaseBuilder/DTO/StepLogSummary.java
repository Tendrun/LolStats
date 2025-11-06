package com.backendwebsite.DatabaseBuilder.DTO;

import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record StepLogSummary(
        String stepName,
        long averageExecutionTimeMs,
        long totalExecutionTimeMs,
        int successCount,
        int failedCount,
        String sentAt,
        List<StepLogDetail> successLogs,
        List<StepLogDetail> failedLogs
) {
    public record StepLogDetail(
            String id,
            String message,
            long executionTimeMs,
            StepsOrder.RequestStatus status
    ) {}

    public static StepLogSummary fromStepLogs(String stepName, List<StepLog> logs) {
        List<StepLog> successLogs = logs.stream()
                .filter(log -> log.requestStatus() == StepsOrder.RequestStatus.SUCCESSFUL)
                .toList();

        List<StepLog> failedLogs = logs.stream()
                .filter(log -> log.requestStatus() == StepsOrder.RequestStatus.FAILED)
                .toList();

        long totalExecutionTime = logs.stream()
                .mapToLong(StepLog::executionTimeMs)
                .sum();

        long averageExecutionTime = logs.isEmpty() ? 0 : totalExecutionTime / logs.size();

        String sentAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<StepLogDetail> successDetails = successLogs.stream()
                .map(log -> new StepLogDetail(log.id(), log.message(), log.executionTimeMs(), log.requestStatus()))
                .toList();

        List<StepLogDetail> failedDetails = failedLogs.stream()
                .map(log -> new StepLogDetail(log.id(), log.message(), log.executionTimeMs(), log.requestStatus()))
                .toList();

        return new StepLogSummary(
                stepName,
                averageExecutionTime,
                totalExecutionTime,
                successLogs.size(),
                failedLogs.size(),
                successDetails,
                failedDetails
        );
    }
}

