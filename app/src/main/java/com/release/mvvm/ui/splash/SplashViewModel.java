package com.release.mvvm.ui.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.release.mvvm.R;
import com.release.mvvm.binding.command.BindingAction;
import com.release.mvvm.binding.command.BindingCommand;
import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.ui.base.Constants;
import com.release.mvvm.ui.guide.GuideActivity;
import com.release.mvvm.ui.home.MainActivity;
import com.release.mvvm.utils.AppManager;
import com.release.mvvm.utils.InstallUtil;
import com.release.mvvm.utils.LogUtils;
import com.release.mvvm.utils.RxHelper;
import com.release.mvvm.utils.SPUtil;
import com.release.mvvm.utils.baserx.CommonSubscriber;
import com.release.mvvm.utils.baserx.RxUtil;
import com.release.mvvm.widget.dialog.NoticeDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author Mr.release
 * @create 2019/4/26
 * @Describe
 */
public class SplashViewModel extends BaseViewModel {
    private static final String TAG = SplashViewModel.class.getSimpleName();

    public ObservableField<String> btnJump = new ObservableField<>("跳过");
    public ObservableInt btnPermissionVisibility = new ObservableInt();
    public ObservableInt btnJumpVisibility = new ObservableInt();
    private boolean isBack;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        btnJumpVisibility.set(View.GONE);
    }

    public BindingCommand jumpClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (Build.VERSION.SDK_INT >= 23 && !hasPermission)
                requestCameraPermissions(mActivity);
            else
                goHome();
        }
    });

    public BindingCommand permissionClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            requestCameraPermissions(mActivity);
        }
    });


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBack = true;
        exit(getApplication());
    }

    public void jump() {
        Boolean loginFirst = (Boolean) SPUtil.getParam(Constants.LOGIN_FIRST, true);
        btnPermissionVisibility.set(View.GONE);
        if (loginFirst) {
            SPUtil.setParam(Constants.LOGIN_FIRST, false);
            goGuide();
        } else {
            btnJumpVisibility.set(View.VISIBLE);
            countdown(6);
        }
    }

    @SuppressLint("CheckResult")
    public void countdown(int time) {
        RxHelper.countdown(time)
                .compose(RxUtil.bindToLifecycle(getLifecycleProvider()))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.exceptionTransformer())
                .doOnSubscribe(subscription -> btnJump.set("跳过(6)"))
                .subscribeWith(new CommonSubscriber<Long>() {
                    @Override
                    protected void _onNext(Long aLong) {
                        btnJump.set("跳过(" + aLong + ")");
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtils.e(TAG, "_onError: " + message);
                    }

                    @Override
                    protected void _onComplete() {
                        LogUtils.i(TAG, "_onComplete: ");
                        if (!isBack)
                            goHome();
                    }
                });
    }

    private void goGuide() {
        startActivity(GuideActivity.class);
    }

    private void goHome() {
        startActivity(MainActivity.class);
    }

    public void exit(Context context) {
        AppManager.appExit(context);
    }


    //-----------------------------------------------Permission--------------------------------------------
    private boolean isNever;
    private boolean hasPermission;
    private SplashActivity mActivity;

    @SuppressLint("CheckResult")
    public void requestCameraPermissions(SplashActivity activity) {
        mActivity = activity;
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEachCombined
                (Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        hasPermission = true;
                        jump();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        showNotice(activity, activity.getResources().getString(R.string.rationale_wr), isNever);
                    } else {
                        isNever = true;
                        showNotice(activity, activity.getResources().getString(R.string.rationale_ask_again), isNever);
                    }
                });

    }

    public void showNotice(SplashActivity context, String content, boolean isNever) {
        NoticeDialog.show(context, content, (v -> noticeListener(context, v, isNever)));
    }

    public void noticeListener(SplashActivity context, View v,  boolean isNever) {
        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.tv_cancel:
                btnPermissionVisibility.set(View.VISIBLE);
                break;
            case R.id.tv_ok:
                if (isNever)
                    InstallUtil.startAppSettings(context);
                else
                    requestCameraPermissions(mActivity);
                break;
        }
        NoticeDialog.dismissDialog();
    }
}
