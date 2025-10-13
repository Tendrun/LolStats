package com.backendwebsite.DatabaseBuilder.Step;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

import java.util.*;

public final class StepsOrder<C extends IContext> {
    public final ArrayList<IStep<C>> steps;
    public StepsOrder(List<IStep<C>> steps) {
        this.steps = new ArrayList<>(steps);
    }

    public enum RequestStatus {
        SUCCESSFUL,
        SKIPPED,
        FAILED
    }
}
