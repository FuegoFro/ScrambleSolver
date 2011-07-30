package com.kat.is.amazing.injected;

import com.google.inject.Module;
import roboguice.application.RoboApplication;

import java.util.List;

public class KatsApplication extends RoboApplication {
    @Override
    protected void addApplicationModules(List<Module> modules) {
        super.addApplicationModules(modules);
    }
}
