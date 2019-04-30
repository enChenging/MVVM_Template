package com.release.mvvm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.release.mvvm.dao.DaoSession;
import com.release.mvvm.dao.NewsTypeDao;
import com.release.mvvm.utils.CrashHandler;
import com.release.mvvm.utils.SPUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class App extends Application implements HasActivityInjector {
    private static final String TAG = App.class.getSimpleName();
    public static App mContext;

    @Inject
    DaoSession mDaoSession;
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    public static App getInstance() {
        return mContext;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().create(this).inject(this);
        mContext = this;
        init();
    }

    private void init() {
        SPUtil.getInstance(this);
        CrashHandler.getInstance().init(getApplicationContext());
        NewsTypeDao.updateLocalData(this, mDaoSession);
    }

    /**
     * token过期
     */
    public void tokenExpire() {
    }
}
