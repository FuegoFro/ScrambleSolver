package com.aloe.scramblesolver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity extends RoboActivity {
    private static final String TAG = "Scramble Solver Activity";
    @InjectView(R.id.start_service) Button startService;
    @InjectView(R.id.stop_service) Button stopService;
    @InjectView(R.id.take_screenshot) Button takeScreenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        startService.setOnClickListener(new StartServiceClickListener());
        stopService.setOnClickListener(new StopServiceClickListener());
        takeScreenshot.setOnClickListener(new TakeScreenshotClickListener());

    }

    private class StartServiceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startService(new Intent(HomeActivity.this, ScreenshotService.class));
        }
    }

    private class StopServiceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            stopService(new Intent(HomeActivity.this, ScreenshotService.class));
        }
    }

    private class TakeScreenshotClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ScreenshotTaker.takeScreenshot();
        }
    }
}
