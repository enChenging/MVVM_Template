package com.release.login.dagger;

import com.release.base.dagger.scope.ActivityScope;
import com.release.login.dagger.guide_module.GuideActivityModule;
import com.release.login.dagger.splash_module.SplashActivityModule;
import com.release.login.ui.guide.GuideActivity;
import com.release.login.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Mr.release
 * @create 2019/4/18
 * @Describe
 */
@Module(includes = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class})
public abstract class LoginAppModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity splashActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = GuideActivityModule.class)
    abstract GuideActivity guideActivityInjector();

}
