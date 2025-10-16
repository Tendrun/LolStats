package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.IContext;

public interface IBuilder<C extends IContext> {
    void build(C context);
}
