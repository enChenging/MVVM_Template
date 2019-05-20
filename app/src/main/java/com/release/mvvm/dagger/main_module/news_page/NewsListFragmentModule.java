package com.release.mvvm.dagger.main_module.news_page;


import androidx.fragment.app.Fragment;

import com.release.base.dagger.module.BaseChildFragmentModule;
import com.release.base.dagger.qualifiers.ChildFragment;
import com.release.base.dagger.scope.ChildFragmentScope;
import com.release.mvvm.ui.adapter.NewsListAdapter;
import com.release.mvvm.ui.page.news_page.NewsListFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */
@Module(includes = {BaseChildFragmentModule.class})
public abstract class NewsListFragmentModule {

    @Binds
    @ChildFragment
    @ChildFragmentScope
    abstract Fragment fragment(NewsListFragment newsListFragment);


    @Provides
    @ChildFragmentScope
    static NewsListAdapter newsListAdapter() {
        return new NewsListAdapter(null);
    }
}
