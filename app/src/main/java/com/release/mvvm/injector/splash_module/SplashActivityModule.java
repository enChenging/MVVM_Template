package com.release.mvvm.injector.splash_module;

import com.release.mvvm.injector.base.ActivityScope;
import com.release.mvvm.injector.base.BaseActivityModule;
import com.release.mvvm.ui.splash.SplashActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import dagger.Binds;
import dagger.Module;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */

@Module(includes = {BaseActivityModule.class})
public abstract class SplashActivityModule {

    @Binds
    @ActivityScope
    abstract RxAppCompatActivity activity(SplashActivity activity);
}
