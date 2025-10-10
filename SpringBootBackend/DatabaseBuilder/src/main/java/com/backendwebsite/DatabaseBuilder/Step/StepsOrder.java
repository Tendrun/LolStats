package com.backendwebsite.DatabaseBuilder.Step;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

import java.util.*;

public final class StepsOrder<C extends IContext> {
    public final LinkedHashMap<Integer, IStep<C>> steps;
    public StepsOrder(Map<Integer, IStep<C>> steps) {
        this.steps = new LinkedHashMap<>(steps);
    }

    public enum RequestStatus {
        SUCCESSFUL,
        SKIPPED,
        FAILED
    }
}
