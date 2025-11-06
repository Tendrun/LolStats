package com.backendwebsite.DatabaseBuilder.DTO.AppApi.Summary;

import java.util.Map;

public record LogSummary(Map<String, StepLogSummary> stepLogSummary, String sentAt) {  }