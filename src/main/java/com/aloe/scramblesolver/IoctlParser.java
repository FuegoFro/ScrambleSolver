package com.aloe.scramblesolver;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IoctlParser {
    public int xres;
    public int yres;
    public int xres_virtual;
    public int yres_virtual;
    public int xoffset;
    public int yoffset;
    public int bitsPerPixel;
    public int grayscale;
    public int redOffset;
    public int redLength;
    public int redMsbRight;
    public int greenOffset;
    public int greenLength;
    public int greenMsbRight;
    public int blueOffset;
    public int blueLength;
    public int blueMsbRight;
    public int transpOffset;
    public int transpLength;
    public int transpMsbRight;

    private static final Pattern GET_RETURN_BUFFER = Pattern.compile("^return buf: (.+)$");

    public IoctlParser(File ioctlOutput) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ioctlOutput)));
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = GET_RETURN_BUFFER.matcher(line);
                if (matcher.find()) {
                    String raw = matcher.group(1);
                    String[] bits = raw.split(" ");
                    int[] values = new int[20];
                    for (int i = 0, s = bits.length / 4; i < s; i++) {
                        String num = "";
                        for (int j = 0; j < 4; j++) {
                            num += bits[(4 * i) + (3 - j)];
                            values[i] = Integer.parseInt(num, 16);
                        }
                    }
                    xres           = values[0];
                    yres           = values[1];
                    xres_virtual   = values[2];
                    yres_virtual   = values[3];
                    xoffset        = values[4];
                    yoffset        = values[5];
                    bitsPerPixel   = values[6];
                    grayscale      = values[7];
                    redOffset      = values[8];
                    redLength      = values[9];
                    redMsbRight    = values[10];
                    greenOffset    = values[11];
                    greenLength    = values[12];
                    greenMsbRight  = values[13];
                    blueOffset     = values[14];
                    blueLength     = values[15];
                    blueMsbRight   = values[16];
                    transpOffset   = values[17];
                    transpLength   = values[18];
                    transpMsbRight = values[19];
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ioctlOutput.delete();
        }
    }
}
