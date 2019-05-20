package com.release.login.dagger.splash_module;

import androidx.appcompat.app.AppCompatActivity;

import com.release.base.dagger.scope.ActivityScope;
import com.release.base.dagger.module.BaseActivityModule;
import com.release.login.ui.splash.SplashActivity;

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
    abstract AppCompatActivity activity(SplashActivity activity);
}
