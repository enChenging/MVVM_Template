package com.release.login.ui.splash;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.release.base.base.BaseActivity;
import com.release.base.utils.StatusBarUtil;
import com.release.login.BR;
import com.release.login.R;
import com.release.login.databinding.ActivitySplashBinding;


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
    public int initVariableId() {
        return BR.viewModel;
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
        viewModel.btnPermissionVisibility.set(View.GONE);
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
