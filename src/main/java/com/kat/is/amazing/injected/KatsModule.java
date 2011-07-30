package com.kat.is.amazing.injected;

import android.content.ContextWrapper;
import roboguice.config.AbstractAndroidModule;

public class KatsModule extends AbstractAndroidModule {
    @Override
    protected void configure() {
        bind(ContextWrapper.class).toProvider(ContextWrapperProvider.class);
    }
}
