package com.release.login.debug;

import com.release.login.dagger.LoginAppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;

/**
 * @author Mr.release
 * @create 2019/4/18
 * @Describe
 */

@Singleton
@Component(modules = LoginAppModule.class)
interface LoginAppComponent extends AndroidInjector<LoginApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<LoginApplication> {

    }
}

