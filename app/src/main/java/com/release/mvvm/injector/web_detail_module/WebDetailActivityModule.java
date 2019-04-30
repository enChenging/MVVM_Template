package com.release.mvvm.injector.web_detail_module;

import com.release.mvvm.injector.base.ActivityScope;
import com.release.mvvm.injector.base.BaseActivityModule;
import com.release.mvvm.ui.web_detail.WebDetailActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import dagger.Binds;
import dagger.Module;

/**
 * @author Mr.release
 * @create 2019/4/25
 * @Describe
 */
@Module(includes = {BaseActivityModule.class})
public abstract class WebDetailActivityModule {
    @Binds
    @ActivityScope
    abstract RxAppCompatActivity activity(WebDetailActivity activity);

}
