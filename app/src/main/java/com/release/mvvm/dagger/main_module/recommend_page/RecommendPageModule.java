package com.release.mvvm.dagger.main_module.recommend_page;

import androidx.fragment.app.Fragment;

import com.release.base.dagger.module.BaseFragmentModule;
import com.release.base.dagger.qualifiers.Fragmentq;
import com.release.base.dagger.scope.FragmentScope;
import com.release.mvvm.R;
import com.release.mvvm.ui.adapter.RecommendAdapter;
import com.release.mvvm.ui.home.MainActivity;
import com.release.mvvm.ui.page.recommend_page.RecommendPage;

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
    @Fragmentq
    @FragmentScope
    abstract Fragment fragment(RecommendPage recommendPage);

    @Provides
    @FragmentScope
    static RecommendAdapter recommendAdapter(MainActivity activity) {
        return new RecommendAdapter(R.layout.item_recommend, null);
    }
}
