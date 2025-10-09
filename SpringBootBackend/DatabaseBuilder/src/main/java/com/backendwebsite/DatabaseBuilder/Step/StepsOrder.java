package com.backendwebsite.DatabaseBuilder.Step;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

import java.util.*;

public final class StepsOrder<C extends IContext> {
    public final LinkedHashMap<Integer, IStep<C>> steps;
    private final Map<Integer, List<Success>> succeeded = new LinkedHashMap<>();
    private final Map<Integer, List<Failure>> failed = new LinkedHashMap<>();
    public StepsOrder(Map<Integer, IStep<C>> steps) {
        this.steps = new LinkedHashMap<>(steps);
    }


    public void markFailure(int stepId, String entityId, String column, String code, String message, boolean skippable,
                            Throwable cause) {
        failed.computeIfAbsent(stepId, k -> new ArrayList<>())
                .add(new Failure(entityId, column, code, message, skippable, cause));
    }

    public void markSuccess(int stepId, String entityId) {
        succeeded.computeIfAbsent(stepId, k -> new ArrayList<>()).add(new Success(entityId));
    }

    public List<Success> getSucceeded(int stepId) {
        return succeeded.getOrDefault(stepId, List.of());
    }

    public List<Failure> getFailed(int stepId) {
        return failed.getOrDefault(stepId, List.of());
    }

    public Map<Integer, List<Success>> allSucceeded() { return Collections.unmodifiableMap(succeeded); }
    public Map<Integer, List<Failure>> allFailed() { return Collections.unmodifiableMap(failed); }

    public void resetResults() {
        succeeded.clear();
        failed.clear();
    }

    public static final class Success {
        private final String entityId;
        public Success(String entityId) { this.entityId = entityId; }
        public String entityId() { return entityId; }
    }

    public static final class Failure {
        private final String entityId;
        private final String column;
        private final String code;
        private final String message;
        private final boolean skippable;
        private final Throwable cause;

        public Failure(String entityId, String column, String code, String message, boolean skippable, Throwable cause) {
            this.entityId = entityId;
            this.column = column;
            this.code = code;
            this.message = message;
            this.skippable = skippable;
            this.cause = cause;
        }

        public String entityId() { return entityId; }
        public String column() { return column; }
        public String code() { return code; }
        public String message() { return message; }
        public boolean skippable() { return skippable; }
        public Throwable cause() { return cause; }
    }
}
