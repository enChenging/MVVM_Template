package com.release.mvvm.injector.main_module.news_page;

import com.release.mvvm.injector.base.BaseFragmentModule;
import com.release.mvvm.injector.base.ChildFragmentScope;
import com.release.mvvm.injector.base.Fragment;
import com.release.mvvm.injector.base.FragmentScope;
import com.release.mvvm.ui.page.news_page.NewsListFragment;
import com.release.mvvm.ui.page.news_page.NewsPage;
import com.trello.rxlifecycle3.components.support.RxFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Mr.release
 * @create 2019/4/18
 * @Describe
 */
@Module(includes = {BaseFragmentModule.class})
public abstract class NewsPageModule {

    @Binds
    @Fragment
    @FragmentScope
    abstract RxFragment fragment(NewsPage newsPage);

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = NewsListFragmentModule.class)
    abstract NewsListFragment newsListFragmentInjector();


}
