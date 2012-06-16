package com.aloe.scramblesolver.injected;

import android.content.Context;
import android.content.ContextWrapper;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ContextWrapperProvider implements Provider<ContextWrapper> {
    @Inject Context context;

    @Override
    public ContextWrapper get() {
        return new ContextWrapper(context);
    }
}
