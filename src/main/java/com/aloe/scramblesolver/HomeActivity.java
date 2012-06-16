package com.aloe.scramblesolver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.*;

public class HomeActivity extends RoboActivity {
    public static final String TAG = "Scramble Solver";
    @InjectView(R.id.screenshot_button) Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        button.setOnClickListener(new ScreenshotTaker());
    }

    class ScreenshotTaker implements View.OnClickListener {

        @Override
        public void onClick(View button) {
            try {
                String sdcardPath = Environment.getExternalStorageDirectory()
                        .toString();

                File frame = new File(sdcardPath, "frame.raw");
                File ioctl = new File(sdcardPath, "ioctl.raw");

//                if (!frame.exists()) {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream stdin = new DataOutputStream(process.getOutputStream());

                // Todo: put this in a temp directory/make sure you're not overwriting something
                stdin.writeBytes("cat /dev/graphics/fb0 > " + frame.getAbsolutePath() + "\n");
                stdin.flush();
                Log.e(TAG, "Got input stream");

                stdin.writeBytes("ioctl -rl 80 /dev/graphics/fb0 17920 > " + ioctl.getAbsolutePath() + "\n");
                stdin.flush();
                Log.e(TAG, "Got ioctl");

                stdin.writeBytes("exit\n");
                Log.e(TAG, "Exit");
                stdin.flush();
                Log.e(TAG, "Flushed");
                process.waitFor();
                Log.e(TAG, "Process Done");
//                }

                IoctlParser frameBufferData = new IoctlParser(ioctl);

                int size = frameBufferData.xres * frameBufferData.yres * frameBufferData.bitsPerPixel * 2;
                byte[] pixelBuffer = new byte[size];
                BufferedInputStream screenshot = new BufferedInputStream(new FileInputStream(frame));
                assert screenshot.read(pixelBuffer, 0, size) == size;
                assert false;


                Bitmap.Config config;
                if (frameBufferData.bitsPerPixel == 16) {
                    config = Bitmap.Config.RGB_565;
                } else if (frameBufferData.bitsPerPixel == 32) {
                    config = Bitmap.Config.ARGB_8888;
                    // USE ARGB_8888 -- need to normalize pixel order
                    if (frameBufferData.transpOffset != 24 ||
                            frameBufferData.redOffset != 16 ||
                            frameBufferData.greenOffset != 8 ||
                            frameBufferData.blueOffset != 0) {
                        throw new RuntimeException("We don't support this format yet");
                    }
                }


                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                int screenshotSize = width * height * 2;

                Log.e(TAG, "Got screen size, reading buffer");

//                byte[] buffer = new byte[screenshotSize];
//                BufferedInputStream is = new BufferedInputStream(new FileInputStream(new File(sdcardPath, "frame.raw")));
//                int read = is.read(buffer, 0, screenshotSize);
//
//                Log.e(TAG, "Read " + read + " bytes to buffer, converting to int[]");
//
//                ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize);
//                bb.order(ByteOrder.nativeOrder());
//                bb.put(buffer);
//                int[] pixels = new int[screenshotSize];
//                bb.asIntBuffer().get(pixels);
//
//                Log.e(TAG, "Converted to int[], setting on Bitmap");

                Log.e(TAG, "Reading in and converting bitmap");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.outHeight = height;
                options.outWidth = width;

                Bitmap bm = BitmapFactory.decodeFile(frame.getPath(), options);

                if (bm == null) {
                    Log.e(TAG, "bm is null");
                    return;
                }

                Log.e(TAG, "Saving bitmap to sdcard");

                OutputStream fOut = null;
                File file = new File(sdcardPath, "screentest.jpg");
                fOut = new FileOutputStream(file);

                bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();

                Log.e("ImagePath", "Image Path : "
                        + MediaStore.Images.Media.insertImage(
                        getContentResolver(), file.getAbsolutePath(),
                        file.getName(), file.getName()));

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }

        }
    }
}
