package chinanurse.cn.nurse.utils;

import android.util.Log;

/**
 * Created by zhuchongkun on 2016/12/8.
 */

public class LogUtils {
    private static final boolean d =false;

    public static void i(String tag, String message) {
        if (d) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (d) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (d) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable tr){
        if (d) {
            Log.e(tag, message, tr);
        }
    }
    public static void v(String tag, String message) {
        if (d) {
            Log.v(tag, message);
        }
    }

    public static void w(String tag, String message) {
        // TODO Auto-generated method stub
        if (d) {
            Log.w(tag, message);
        }
    }
}
