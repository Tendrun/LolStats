package com.backendwebsite.DatabaseBuilder.Pipeline;

import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;

import java.util.List;

public final class Pipeline {
    private Pipeline() {}
    public static <C extends IContext> void executeSteps(
             StepsOrder<C> stepsOrders, C context) {
        for (IStep<C> step : stepsOrders.steps) step.execute(context);
    }
}