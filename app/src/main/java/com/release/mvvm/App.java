package com.release.mvvm;

import android.app.Activity;

import com.release.base.base.BaseApplication;
import com.release.base.config.ModuleLifecycleConfig;
import com.release.mvvm.dao.DaoSession;
import com.release.mvvm.dao.NewsTypeDao;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @author Mr.release
 * @create 2019/3/22
 *
 *
 *
 * @Describe
 */
public class App extends BaseApplication implements HasActivityInjector {

    @Inject
    DaoSession mDaoSession;
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().create(this).inject(this);
        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);
        init();
    }

    private void init() {
        NewsTypeDao.updateLocalData(this, mDaoSession);
    }
}
