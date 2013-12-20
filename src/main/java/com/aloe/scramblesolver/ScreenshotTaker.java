package com.aloe.scramblesolver;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class ScreenshotTaker {
    private static final String TAG = "Screenshot Taker";

    public static void takeScreenshot() {
        try {
            Log.e(TAG, "START");
            String screenshotLocation = "/sdcard/screentest.png";

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream stdin = new DataOutputStream(process.getOutputStream());

            stdin.writeBytes("screencap -p " + screenshotLocation + "\n");

            stdin.writeBytes("exit\n");
            stdin.flush();
            process.waitFor();

            Log.e(TAG, "DONE");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
