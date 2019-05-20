package com.release.mvvm.dagger.main_module;

import androidx.appcompat.app.AppCompatActivity;

import com.release.base.dagger.module.BaseActivityModule;
import com.release.base.dagger.scope.ActivityScope;
import com.release.base.dagger.scope.FragmentScope;
import com.release.mvvm.dagger.main_module.kinds_page.KindsPageModule;
import com.release.mvvm.dagger.main_module.news_page.NewsPageModule;
import com.release.mvvm.dagger.main_module.recommend_page.RecommendPageModule;
import com.release.mvvm.dagger.main_module.video_page.VideoPageModule;
import com.release.mvvm.ui.home.MainActivity;
import com.release.mvvm.ui.page.kinds_page.KindsPage;
import com.release.mvvm.ui.page.news_page.NewsPage;
import com.release.mvvm.ui.page.recommend_page.RecommendPage;
import com.release.mvvm.ui.page.video_page.VideoPage;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Mr.release
 * @create 2019/4/18
 * @Describe
 */
@Module(includes = {BaseActivityModule.class})
public abstract class MainActivityModule {

    @Binds
    @ActivityScope
    abstract AppCompatActivity activity(MainActivity activity);

    @FragmentScope
    @ContributesAndroidInjector(modules = NewsPageModule.class)
    abstract NewsPage newsPageInjector();

    @FragmentScope
    @ContributesAndroidInjector(modules = VideoPageModule.class)
    abstract VideoPage videoPageInjector();

    @FragmentScope
    @ContributesAndroidInjector(modules = RecommendPageModule.class)
    abstract RecommendPage recommendPageInjector();

    @FragmentScope
    @ContributesAndroidInjector(modules = KindsPageModule.class)
    abstract KindsPage kindsPageInjector();


}
