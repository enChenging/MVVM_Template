package com.release.mvvm.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.utils.AppManager;
import com.release.mvvm.utils.ToastUtils;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class MainViewModel extends BaseViewModel {

    private long mExitTime = 0;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }


    public void toggle(DrawerLayout mDlDrawer) {
        int drawerLockMode = mDlDrawer.getDrawerLockMode(GravityCompat.START);
        if (mDlDrawer.isDrawerVisible(GravityCompat.START) && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            mDlDrawer.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mDlDrawer.openDrawer(GravityCompat.START);
        }
    }

    public void exit(boolean isCloseDrawableLayout) {

        if (!isCloseDrawableLayout) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.show("再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else
                AppManager.appExit(getApplication());
        }
    }
}
