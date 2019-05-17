package com.release.mvvm.dagger.main_module.video_page;

import androidx.fragment.app.FragmentManager;

import com.release.base.base.ViewPagerAdapter;
import com.release.base.dagger.module.BaseFragmentModule;
import com.release.base.dagger.qualifiers.ChildFragmentManager;
import com.release.base.dagger.scope.ChildFragmentScope;
import com.release.base.dagger.qualifiers.Fragment;
import com.release.base.dagger.scope.FragmentScope;
import com.release.mvvm.ui.page.video_page.VideoListFragment;
import com.release.mvvm.ui.page.video_page.VideoPage;
import com.trello.rxlifecycle3.components.support.RxFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Mr.release
 * @create 2019/4/18
 * @Describe
 */
@Module(includes = {BaseFragmentModule.class})
public abstract class VideoPageModule {

    @Binds
    @Fragment
    @FragmentScope
    abstract RxFragment fragment(VideoPage videoPage);

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = VideoListFragmentModule.class)
    abstract VideoListFragment videoListFragment();

    @Provides
    @FragmentScope
    static ViewPagerAdapter viewPagerAdapter(@ChildFragmentManager FragmentManager fragmentManager) {
        return new ViewPagerAdapter(fragmentManager);
    }
}
