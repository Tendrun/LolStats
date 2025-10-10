package com.backendwebsite.DatabaseBuilder.Step;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

import java.util.List;

public interface IStep<T extends IContext> {
    void execute(T context);
}
