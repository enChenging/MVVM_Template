package com.release.mvvm.dagger.news_detail_module;

import androidx.appcompat.app.AppCompatActivity;

import com.release.base.dagger.module.BaseActivityModule;
import com.release.base.dagger.scope.ActivityScope;
import com.release.mvvm.ui.page.news_page.NewsDetailActivity;

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
    abstract AppCompatActivity activity(NewsDetailActivity activity);

}
