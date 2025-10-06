package com.backendwebsite.DatabaseBuilder.Pipeline;

import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;

import java.util.List;

public final class Pipeline {
    private Pipeline() {}
    public static <C extends IContext> void executeSteps(
            List<? extends IStep<C>> steps, C context) {
        for (IStep<C> step : steps) step.execute(context);
    }
}

