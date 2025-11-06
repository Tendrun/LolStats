package com.backendwebsite.DatabaseBuilder.Util;

import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;

/**
 * Helper klasa do formatowania logów w czytelnym formacie
 */
public class LogFormatter {

    /**
     * Format: [STEP_NAME] [STATUS] - Message | ExecutionTime: Xms
     */
    public static String formatStepLog(String stepName, StepsOrder.RequestStatus status, String message, long executionTimeMs) {
        String statusStr = status == StepsOrder.RequestStatus.SUCCESSFUL ? "✓ SUCCESS" : "✗ FAILED";
        return String.format("[%s] %s - %s | Time: %dms", stepName, statusStr, message, executionTimeMs);
    }

    /**
     * Format: [STEP_NAME] [STATUS] - puuid: XXXX | ExecutionTime: Xms
     */
    public static String formatStepLogWithPuuid(String stepName, StepsOrder.RequestStatus status, String puuid, long executionTimeMs) {
        String statusStr = status == StepsOrder.RequestStatus.SUCCESSFUL ? "✓" : "✗";
        return String.format("[%s] %s puuid: %s | Time: %dms", stepName, statusStr, extractPuuid(puuid), executionTimeMs);
    }

    /**
     * Format dla summary: StepName | Success: X | Failed: Y | Avg Time: Xms | Total Time: Xms
     */
    public static String formatSummary(String stepName, int successCount, int failedCount, long avgTime, long totalTime) {
        return String.format("[%s] ✓:%d ✗:%d | Avg: %dms | Total: %dms", stepName, successCount, failedCount, avgTime, totalTime);
    }

    /**
     * Ekstraktuje tylko puuid z pełnego ID
     */
    private static String extractPuuid(String id) {
        if (id == null || id.isEmpty()) {
            return "N/A";
        }
        if (id.contains(":")) {
            String[] parts = id.split(":");
            return parts[parts.length - 1];
        }
        return id;
    }

    /**
     * Zkraja puuid do 8 znaków dla czytelności
     */
    public static String shortPuuid(String puuid) {
        if (puuid == null || puuid.length() <= 8) {
            return puuid;
        }
        return puuid.substring(0, 8) + "...";
    }
}

