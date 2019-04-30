package com.release.mvvm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.release.mvvm.App;


/**
 * @author Mr.release
 * @create 2017/3/22
 * @Describe
 */
public class KeyboardUtils {
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) App.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) App.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void showKeyboardPopWindow() {
        InputMethodManager imm = (InputMethodManager) App.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    static boolean isShow;

    private boolean getKeyboardIsShow(Dialog dialog) {

        final View decorView = dialog.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();

                if (displayHight > hight / 3 * 2) {
                    //键盘隐藏
                    isShow = false;
                } else {
                    //键盘显示
                    isShow = true;
                }
            }
        });

        return isShow;
    }


    public static boolean getKeyboardIsShow(Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();

                if (displayHight > hight / 3 * 2) {
                    //键盘隐藏
                    isShow = false;
                } else {
                    //键盘显示
                    isShow = true;
                }
            }
        });

        return isShow;
    }
}
