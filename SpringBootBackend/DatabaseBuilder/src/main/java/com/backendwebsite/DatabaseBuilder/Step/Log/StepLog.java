package com.backendwebsite.DatabaseBuilder.Step.Log;

import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;

public record StepLog(StepsOrder.RequestStatus requestStatus, String stepName, String message) { }