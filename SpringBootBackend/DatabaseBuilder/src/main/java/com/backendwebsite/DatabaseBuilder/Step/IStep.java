package com.backendwebsite.DatabaseBuilder.Step;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

public interface IStep<T extends IContext> {
    void execute(T context);
}
