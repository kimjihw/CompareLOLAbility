package kr.co.simplebestapp.LolApiTest.utis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Util {

    public static boolean ISLOG;
    public static String appName = "simple_compass";
    public static boolean isOneStore = false;

    static {
        ISLOG = false;
    }

    public static boolean createMoviesFolder(Context c) throws IOException {

        boolean res = false;

        File moviesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File appFolder = new File(moviesPath, appName);

        if(!appFolder.exists()){

            res = appFolder.mkdirs();

        } else {

            res = true;

        }

        return res;

    }

    public static File createVideoFile(Context c, String tmpName) throws IOException {

        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File appFolder = new File(downloadPath + "/" + appName);

        if(!appFolder.exists()){
            appFolder.mkdirs();
        }

        return new File(appFolder, tmpName);

    }

    public static String getMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + appName;
    }

    public static int getDpToPixel(Context context, float dp) {
        // Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android

        float px = 0;

        try {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        } catch (Exception e) {

        }

        return (int) px;

    }

    /**
     * 화면 기준으로 계산하여 가로 사이즈 구하기
     *
     * @param activity
     * @param blankSize padding margine값들의 합
     * @param divide    가로 사이즈 구할때 몇 분할기준으로 할껀지.(스크린에서 3등분했을때의 각각의 가로 사이즈를 구하고 싶을때)
     * @return
     */
    public static int getWantWidthOfScreen(@NonNull Activity activity, float blankSize, int divide) {

        int image_Width = 0;
        int colBlankSize = (int) Math.round((getDpToPixel(activity, blankSize) * 10.0) / 10.0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());

            image_Width = (windowMetrics.getBounds().width() - insets.left - insets.right - colBlankSize) / divide;

        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            image_Width = (displayMetrics.widthPixels - colBlankSize) / divide;

        }

        return image_Width;
    }

    public static String bitmapToFile(Context c, Bitmap bitmap, String fileName) {

        File fileCacheItem = new File(c.getCacheDir(), fileName + ".jpg");
        String strMyImagePath = fileCacheItem.getAbsolutePath();

        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strMyImagePath;
    }

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

    /*public static Boolean isLaunchingService(Context c) {

        ActivityManager manager = (ActivityManager) c.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            //Log.e("Util", "service.service.getClassName() = " + service.service.getClassName());
            //if (AccessibilityStateService.class.getName().equals(service.service.getClassName())) {
                //return true;
            //}
        }

        return false;
    }*/

}
