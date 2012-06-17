package com.aloe.scramblesolver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.*;
import java.util.Arrays;

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
                int[] colorOffsets = new int[4];
                if (frameBufferData.bitsPerPixel == 16) {
                    config = Bitmap.Config.RGB_565;
                } else if (frameBufferData.bitsPerPixel == 32) {
                    config = Bitmap.Config.ARGB_8888;
                    // USE ARGB_8888 -- need to normalize pixel order
                    colorOffsets[frameBufferData.transpOffset / 8] = 24;
                    colorOffsets[frameBufferData.redOffset / 8] = 16;
                    colorOffsets[frameBufferData.greenOffset / 8] = 8;
                    colorOffsets[frameBufferData.blueOffset / 8] = 0;


                    boolean shouldSwitch = false;
                    for (int i = 0; i < 100; i++) {
                        if (pixelBuffer[(i * 4) + (frameBufferData.transpOffset / 8)] != (byte) 0xff) {
                            int fourFromEnd = i*4;
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


                    Log.e(TAG, "red: " + frameBufferData.redOffset + ", green: " + frameBufferData.greenOffset + ", blue: " + frameBufferData.blueOffset + ", transparent: " + frameBufferData.transpOffset);
                    Log.e(TAG, "colorOffsets: " + Arrays.toString(colorOffsets));
                } else {
                    Log.e(TAG, "bpp: " + frameBufferData.bitsPerPixel);
                    throw new RuntimeException("We don't support this format yet");
                }

                int[] pixels = new int[size / 4];
                for (int i = 0, pixelLength = pixels.length; i < pixelLength; i++) {
                    for (int j = 0; j < 4; j++) {
                        int color = pixelBuffer[(i * 4) + j] & 0xff;
                        pixels[i] |= color << colorOffsets[j];
                    }
                }

                int fourFromEnd = pixelBuffer.length - 4;
                Log.e(TAG, "Last pixel: " + Integer.toHexString(pixels[pixels.length - 1]));
                Log.e(TAG, "last 4 bytes: " +
                        Integer.toHexString(pixelBuffer[fourFromEnd]) + " " +
                        Integer.toHexString(pixelBuffer[fourFromEnd + 1]) + " " +
                        Integer.toHexString(pixelBuffer[fourFromEnd + 2]) + " " +
                        Integer.toHexString(pixelBuffer[fourFromEnd + 3])
                );

//                Log.e(TAG, "Size: " + size + ", pixels: " + pixels.length + ", pixelBuffer: " + pixelBuffer.length);
//                Log.e(TAG, "nonZero: " + nonZero);

                Log.e(TAG, "Reading in and converting bitmap");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = config;
                options.outHeight = frameBufferData.yres;
                options.outWidth = frameBufferData.xres;

                Bitmap bm = Bitmap.createBitmap(frameBufferData.xres, frameBufferData.yres, config);
                bm.setPixels(pixels, 0, frameBufferData.xres, 0, 0, frameBufferData.xres, frameBufferData.yres);

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
