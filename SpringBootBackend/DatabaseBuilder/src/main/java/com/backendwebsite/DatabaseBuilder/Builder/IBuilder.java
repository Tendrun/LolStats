package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;

public interface IBuilder<C extends IContext, R> {
    StepsOrder<C> steps();
    void build(C context);
    R getResult();
}
