package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

public interface IBuilder<C extends IContext, R> {
    void build(C context);
    R getResult();
}
