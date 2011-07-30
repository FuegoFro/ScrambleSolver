package com.kat.is.amazing;

import android.content.ContextWrapper;
import android.content.Intent;
import android.view.View;
import com.google.inject.Inject;

public class StartActivityClickListener implements View.OnClickListener {
    @Inject ContextWrapper contextWrapper;

    private Class<?> activityToStart;

    public void setActivityClass(Class<?> activityToStart) {
        this.activityToStart = activityToStart;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(contextWrapper, activityToStart);
        contextWrapper.startActivity(intent);
    }
}
