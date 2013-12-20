package com.aloe.scramblesolver;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Arrays;

class OverlayView extends FrameLayout {
    private static final String TAG = "Scramble Solver View";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
    public static final String lang = "eng";
    private int x;
    private int y;
    private int height;
    private int width;
    private OverlayView.TakeScreenshotClickListener takeScreenshotClickListener = new TakeScreenshotClickListener();

    public OverlayView(Context context) {
        super(context);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Button button = (Button) findViewById(R.id.screenshot_button);
        button.setOnClickListener(takeScreenshotClickListener);
        int[] location = new int[2];
        button.getLocationOnScreen(location);
        x = location[0];
        y = location[1];
        height = button.getHeight();
        width = button.getWidth();
        Log.e(TAG, "location: " + Arrays.toString(location) + ", height: " + height + ", width: " + width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, event.getX() + ", " + event.getY());
        if (x <= event.getX() && event.getX() <= x + width &&
                y <= event.getY() && event.getY() <= y + height) {
            Log.e(ScreenshotService.TAG, "You touched me! (" + event.getX() + ", " + event.getY() + ")");
            ScreenshotTaker.takeScreenshot();
        }
        return super.onTouchEvent(event);
    }

    class TakeScreenshotClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            ScreenshotTaker.takeScreenshot();
        }
    }
}
