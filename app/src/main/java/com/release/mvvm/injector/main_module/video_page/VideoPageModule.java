package com.release.mvvm.injector.main_module.video_page;

import com.release.mvvm.injector.base.BaseFragmentModule;
import com.release.mvvm.injector.base.ChildFragmentScope;
import com.release.mvvm.injector.base.Fragment;
import com.release.mvvm.injector.base.FragmentScope;
import com.release.mvvm.ui.page.video_page.VideoListFragment;
import com.release.mvvm.ui.page.video_page.VideoPage;
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
public abstract class VideoPageModule {

    @Binds
    @Fragment
    @FragmentScope
    abstract RxFragment fragment(VideoPage videoPage);

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = VideoListFragmentModule.class)
    abstract VideoListFragment videoListFragment();
}
