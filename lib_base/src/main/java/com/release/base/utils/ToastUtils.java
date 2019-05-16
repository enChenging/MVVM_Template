package com.release.base.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.release.base.R;
import com.release.base.base.BaseApplication;


/**
 * 避免同样的信息多次触发重复弹出的问题
 */
public class ToastUtils {

    private static CharSequence oldMsg;
    protected static Toast toast = null;
    private static Toast toast2;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private ToastUtils() {
        throw new RuntimeException("ToastUtils cannot be initialized!");
    }

    public static void showToast(CharSequence s, int duration) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), s, duration);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
            oneTime = twoTime;
        }
    }

    public static void show(int resId) {
        showToast(BaseApplication.getInstance().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public static void showLong(CharSequence message) {
        showToast(message, Toast.LENGTH_LONG);
    }


    public static void showLong(int strResId) {
        showToast(BaseApplication.getInstance().getResources().getText(strResId), Toast.LENGTH_LONG);
    }


    public static void show(CharSequence message, int duration) {
        showToast(message, duration);
    }

    public static void show(int strResId, int duration) {
        showToast(BaseApplication.getInstance().getResources().getText(strResId), duration);
    }

    public static Toast showToastWithImg(final String tvStr, final int imageResource) {
        if (toast2 == null) {
            toast2 = new Toast(BaseApplication.getInstance());
        }
        View view = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.toast_custom, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
        tv.setText(TextUtils.isEmpty(tvStr) ? "" : tvStr);
        ImageView iv = (ImageView) view.findViewById(R.id.toast_custom_iv);
        if (imageResource > 0) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imageResource);
        } else {
            iv.setVisibility(View.GONE);
        }
        toast2.setView(view);
        toast2.setGravity(Gravity.CENTER, 0, 0);
        toast2.show();
        return toast2;

    }
}
