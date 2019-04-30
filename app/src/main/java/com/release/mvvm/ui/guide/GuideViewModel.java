package com.release.mvvm.ui.guide;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.release.mvvm.R;
import com.release.mvvm.binding.command.BindingAction;
import com.release.mvvm.binding.command.BindingCommand;
import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.ui.home.MainActivity;
import com.release.mvvm.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class GuideViewModel extends BaseViewModel {

    public List<ImageView> imageList = new ArrayList<>();
    private int[] images = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    public GuideViewModel(@NonNull Application application) {
        super(application);
    }


    public BindingCommand btHomeClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            goHome();
        }
    });

    public void goHome() {
        startActivity(MainActivity.class);
    }

    public void imageViews(Context context, LinearLayout mDotGroup) {
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(context);
            iv.setBackgroundResource(images[i]);
            imageList.add(iv);

            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(context, 6), DensityUtil.dip2px(context, 6));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(context, 10);
            }
            view.setBackgroundResource(R.drawable.ic_dots_blue);
            view.setLayoutParams(params);
            mDotGroup.addView(view);
        }
    }
}
