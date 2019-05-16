package com.release.login.dagger.guide_module;

import com.release.base.dagger.scope.ActivityScope;
import com.release.base.dagger.module.BaseActivityModule;
import com.release.login.ui.guide.GuideActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import dagger.Binds;
import dagger.Module;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */
@Module(includes = {BaseActivityModule.class})
public abstract class GuideActivityModule {

    @Binds
    @ActivityScope
    abstract RxAppCompatActivity activity(GuideActivity activity);

}
