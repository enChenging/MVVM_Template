package com.release.login.debug;

import android.app.Activity;

import com.release.base.base.BaseApplication;
import com.release.base.config.ModuleLifecycleConfig;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @author Mr.release
 * @create 2019/5/15
 * @Describe
 */
public class LoginApplication extends BaseApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerLoginAppComponent.builder().create(this).inject(this);
        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);
    }

}
