package cronzy.com.cronzypicker.utils;

import android.graphics.Color;
import android.util.Log;

import cronzy.com.cronzypicker.constants.ProjectConstants;

public class Converters  {


    public float[] HValue(String rgb) {
        int r, g, b;
        float[] hsv = new float[3];
        hsv[0] = -1;

        try {
            int rgbI = Integer.valueOf(rgb);
            if (rgb.length() == 9) {
                r = Integer.valueOf(rgb.substring(0, 3));
                g = Integer.valueOf(rgb.substring(3, 6));
                b = Integer.valueOf(rgb.substring(6, rgb.length()));
                Color.RGBToHSV(r, g, b, hsv);
            }
        } catch (Exception nfeEx) {
            Log.d(ProjectConstants.TAG + " " + getClass() + " ", "Exception " + nfeEx.getMessage());
        }
        return hsv;
    }

    public float[] HValue(int r, int g, int b) {
        float[] hsv = new float[3];
        hsv[0] = -1;
        Color.RGBToHSV(r, g, b, hsv);
        return hsv;
    }


    public String convertToHex(int intColor) {
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        return hexColor;
    }
}
