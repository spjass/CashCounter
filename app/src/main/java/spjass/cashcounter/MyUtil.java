package spjass.cashcounter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;

/**
 * Created by Spjass on 14-Jan-16.
 */
public class MyUtil {

    public static String parseCurrency(float decimal) {
        DecimalFormat df = new DecimalFormat("0.00€");
        return df.format(decimal);
    }

    public static String parseAmount(float decimal) {
        DecimalFormat df = new DecimalFormat("0x");
        return df.format(decimal);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static Bitmap eraseBG(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap b = src.copy(Bitmap.Config.ARGB_8888, true);
        b.setHasAlpha(true);

        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            if (pixels[i] == color) {
                pixels[i] = 0;
            }
        }

        b.setPixels(pixels, 0, width, 0, 0, width, height);

        return b;
    }

}
