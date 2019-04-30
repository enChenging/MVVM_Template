package com.release.mvvm.injector.news_detail_module;

import com.release.mvvm.injector.base.ActivityScope;
import com.release.mvvm.injector.base.BaseActivityModule;
import com.release.mvvm.ui.page.news_page.NewsDetailActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import dagger.Binds;
import dagger.Module;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */

@Module(includes = {BaseActivityModule.class})
public abstract class NewsDetailActivityModule {

    @Binds
    @ActivityScope
    abstract RxAppCompatActivity activity(NewsDetailActivity activity);

}
