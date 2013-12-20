package com.aloe.scramblesolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.*;
import java.util.Arrays;

class OverlayView extends FrameLayout {
    private static final String TAG = "Scramble Solver View";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
    public static final String lang = "eng";
    private int x;
    private int y;
    private int height;
    private int width;

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
        button.setOnClickListener(new TakeScreenshotClickListener());
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
            takeScreenshot();
        }
        return super.onTouchEvent(event);
    }

    public void takeScreenshot() {
        try {
            Log.e(TAG, "START");
            // Todo: put this in a temp directory/make sure you're not overwriting something
            String sdcardPath = Environment.getExternalStorageDirectory()
                    .toString();

            File frame = new File(sdcardPath, "frame.raw");
            File ioctl = new File(sdcardPath, "ioctl.raw");

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream stdin = new DataOutputStream(process.getOutputStream());

            stdin.writeBytes("cat /dev/graphics/fb0 > " + frame.getAbsolutePath() + "\n");
            stdin.flush();

            stdin.writeBytes("ioctl -rl 80 /dev/graphics/fb0 17920 > " + ioctl.getAbsolutePath() + "\n");
            stdin.flush();

            stdin.writeBytes("exit\n");
            stdin.flush();
            process.waitFor();

            Log.e(TAG, "Got buffer");

            IoctlParser frameBufferData = new IoctlParser(ioctl);

            int size = frameBufferData.xres * frameBufferData.yres * (frameBufferData.bitsPerPixel / 8);
            byte[] pixelBuffer = new byte[size];
            BufferedInputStream screenshot = new BufferedInputStream(new FileInputStream(frame));

            int read = screenshot.read(pixelBuffer, 0, size);
            if (read != size) {
                throw new RuntimeException("Did not read all teh bytes. Read: " + read);
            }
            Log.e(TAG, "Read = " + read);
            if (!frame.delete()) {
                throw new RuntimeException("Did not delete teh frame");
            }


            Bitmap.Config config;
            int[] pixels = new int[frameBufferData.xres * frameBufferData.yres];
            if (frameBufferData.bitsPerPixel == 16) {
                config = Bitmap.Config.RGB_565;
                throw new RuntimeException("Unsupported format");
            } else if (frameBufferData.bitsPerPixel == 32) {
                config = Bitmap.Config.ARGB_8888;
                // USE ARGB_8888 -- need to normalize pixel order
                int[] colorOffsets = new int[4];
                colorOffsets[frameBufferData.transpOffset / 8] = 24;
                colorOffsets[frameBufferData.redOffset / 8] = 16;
                colorOffsets[frameBufferData.greenOffset / 8] = 8;
                colorOffsets[frameBufferData.blueOffset / 8] = 0;


                boolean shouldSwitch = false;
                for (int i = 0; i < 100; i++) {
                    if (pixelBuffer[(i * 4) + (frameBufferData.transpOffset / 8)] != (byte) 0xff) {
                        int fourFromEnd = i * 4;
                        Log.e(TAG, "transparent pos is: " + (frameBufferData.transpOffset / 8) + " transp byte is: " + Integer.toHexString(pixelBuffer[(i * 4) + (frameBufferData.transpOffset / 8)]));
                        Log.e(TAG, "last 4 bytes: " +
                                Integer.toHexString(pixelBuffer[fourFromEnd]) + " " +
                                Integer.toHexString(pixelBuffer[fourFromEnd + 1]) + " " +
                                Integer.toHexString(pixelBuffer[fourFromEnd + 2]) + " " +
                                Integer.toHexString(pixelBuffer[fourFromEnd + 3])
                        );
                        shouldSwitch = true;
                        break;
                    }
                }

                if (shouldSwitch) {
                    Log.e(TAG, "SWITCHING");
                    int tmp1 = colorOffsets[0];
                    colorOffsets[0] = colorOffsets[3];
                    colorOffsets[3] = tmp1;

                    int tmp2 = colorOffsets[1];
                    colorOffsets[1] = colorOffsets[2];
                    colorOffsets[2] = tmp2;
                }


                for (int i = 0, pixelLength = pixels.length; i < pixelLength; i++) {
                    for (int j = 0; j < 4; j++) {
                        int color = pixelBuffer[(i * 4) + j] & 0xff;
                        pixels[i] |= color << colorOffsets[j];
                    }
                }
            } else {
                Log.e(TAG, "bpp: " + frameBufferData.bitsPerPixel);
                throw new RuntimeException("We don't support this format yet");
            }

            Log.e(TAG, "Reading in and converting bitmap");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = config;
            options.outHeight = frameBufferData.yres;
            options.outWidth = frameBufferData.xres;

            Bitmap bm = Bitmap.createBitmap(frameBufferData.xres, frameBufferData.yres, config);
            bm.setPixels(pixels, 0, frameBufferData.xres, 0, 0, frameBufferData.xres, frameBufferData.yres);

            TessBaseAPI baseApi = new TessBaseAPI();
            Log.e(TAG, "Made base api");
            // DATA_PATH = Path to the storage
            // lang for which the language data exists, usually "eng"
            baseApi.init(DATA_PATH, lang);
            Log.e(TAG, "Initialized base api");
            baseApi.setImage(bm);
            Log.e(TAG, "set image");
            String recognizedText = baseApi.getUTF8Text();
            Log.e(TAG, "recognized text");
            baseApi.end();
            Log.e(TAG, "ended");

            Log.e(TAG, recognizedText);


            Log.e(TAG, "Saving bitmap to sdcard");

            OutputStream fOut = null;
            File file = new File(sdcardPath, "screentest.jpg");
            fOut = new FileOutputStream(file);

            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    class TakeScreenshotClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            takeScreenshot();
        }
    }
}
