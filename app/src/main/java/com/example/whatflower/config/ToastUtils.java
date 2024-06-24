package com.example.whatflower.config;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

    private static ToastUtils instance;
    private Context mContext;
    private static Toast mToast;

    private ToastUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized ToastUtils getInstance(Context mContext) {
        if (instance == null){
            instance = new ToastUtils(mContext);
        }
        return instance;
    }

//    public static void showToast(String message) {
//        if (instance != null) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(instance.mContext, message, Toast.LENGTH_SHORT);
//        mToast.show();
//    }
//
//    public static void showToast(Context context, int resId) {
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
//        mToast.show();
//    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void showToast(final Context context, final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    public static void showToast(final Context context, final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }
}
