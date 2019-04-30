package com.release.mvvm.ui.splash;

import android.os.Build;
import android.os.Bundle;

import com.release.mvvm.R;
import com.release.mvvm.BR;
import com.release.mvvm.databinding.ActivitySplashBinding;
import com.release.mvvm.ui.base.BaseActivity;
import com.release.mvvm.utils.StatusBarUtil;


/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

        if (Build.VERSION.SDK_INT >= 23)
            viewModel.requestCameraPermissions(this);
        else
            viewModel.jump();
    }

    @Override
    public void updateViews(boolean isRefresh) {

    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }


}
