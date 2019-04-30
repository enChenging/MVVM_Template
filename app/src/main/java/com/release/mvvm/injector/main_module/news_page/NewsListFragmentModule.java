package com.release.mvvm.injector.main_module.news_page;


import com.release.mvvm.injector.base.BaseChildFragmentModule;
import com.release.mvvm.injector.base.ChildFragment;
import com.release.mvvm.injector.base.ChildFragmentScope;
import com.release.mvvm.ui.adapter.NewsListAdapter;
import com.release.mvvm.ui.page.news_page.NewsListFragment;
import com.trello.rxlifecycle3.components.support.RxFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */
@Module(includes = {BaseChildFragmentModule.class, NewsListPresenterModule.class})
public abstract class NewsListFragmentModule {

    @Binds
    @ChildFragment
    @ChildFragmentScope
    abstract RxFragment fragment(NewsListFragment newsListFragment);

//    @Binds
//    @ChildFragmentScope
//    abstract NewsListView newsListView(NewsListFragment newsListFragment);

    @Provides
    @ChildFragmentScope
    static NewsListAdapter newsListAdapter() {
        return new NewsListAdapter(null);
    }
}
