package com.release.mvvm.injector.main_module.recommend_page;

import com.release.mvvm.R;
import com.release.mvvm.injector.base.BaseFragmentModule;
import com.release.mvvm.injector.base.Fragment;
import com.release.mvvm.injector.base.FragmentScope;
import com.release.mvvm.ui.adapter.RecommendAdapter;
import com.release.mvvm.ui.home.MainActivity;
import com.release.mvvm.ui.page.recommend_page.RecommendPage;
import com.trello.rxlifecycle3.components.support.RxFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/22
 * @Describe
 */
@Module(includes = {BaseFragmentModule.class})
public abstract class RecommendPageModule {

    @Binds
    @Fragment
    @FragmentScope
    abstract RxFragment fragment(RecommendPage recommendPage);

    @Provides
    @FragmentScope
    static RecommendAdapter recommendAdapter(MainActivity activity) {
        return new RecommendAdapter(R.layout.item_recommend, null);
    }
}
