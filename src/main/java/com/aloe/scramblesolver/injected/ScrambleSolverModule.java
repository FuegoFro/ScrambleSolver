package com.aloe.scramblesolver.injected;

import android.content.ContextWrapper;
import roboguice.config.AbstractAndroidModule;

public class ScrambleSolverModule extends AbstractAndroidModule {
    @Override
    protected void configure() {
        bind(ContextWrapper.class).toProvider(ContextWrapperProvider.class);
    }
}
