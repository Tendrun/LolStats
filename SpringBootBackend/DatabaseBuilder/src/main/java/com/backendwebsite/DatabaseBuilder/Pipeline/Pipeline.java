package com.backendwebsite.DatabaseBuilder.Pipeline;

import com.backendwebsite.DatabaseBuilder.Step.IStep;

public class Pipeline<T> {
    final IStep[] steps;

    public Pipeline(IStep[] steps){
        this.steps = steps;
    }

    public void exectueSteps() {
        for (IStep step : steps) {
            step.execute();
        }
    }
}
