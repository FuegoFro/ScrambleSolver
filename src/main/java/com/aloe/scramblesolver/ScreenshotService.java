package com.aloe.scramblesolver;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class ScreenshotService extends Service {
    public static final String TAG = "Scramble Solver Service";

    private View overlay;

    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        overlay = layoutInflater.inflate(R.layout.overlay_layout, null);
        wm.addView(overlay, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.removeView(overlay);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
