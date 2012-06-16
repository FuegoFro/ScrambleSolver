package com.aloe.scramblesolver.injected;

import com.google.inject.Module;
import roboguice.application.RoboApplication;

import java.util.List;

public class ScrambleSolverApplication extends RoboApplication {
    @Override
    protected void addApplicationModules(List<Module> modules) {
        modules.add(new ScrambleSolverModule());
        super.addApplicationModules(modules);
    }
}
