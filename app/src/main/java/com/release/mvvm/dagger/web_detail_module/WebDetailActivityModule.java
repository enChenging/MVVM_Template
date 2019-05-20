package com.release.mvvm.dagger.web_detail_module;

import androidx.appcompat.app.AppCompatActivity;

import com.release.base.dagger.module.BaseActivityModule;
import com.release.base.dagger.scope.ActivityScope;
import com.release.mvvm.ui.web_detail.WebDetailActivity;

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
    abstract AppCompatActivity activity(WebDetailActivity activity);

}
