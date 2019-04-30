package com.release.mvvm.injector.main_module.video_page;

import com.release.mvvm.R;
import com.release.mvvm.injector.base.BaseChildFragmentModule;
import com.release.mvvm.injector.base.ChildFragment;
import com.release.mvvm.injector.base.ChildFragmentScope;
import com.release.mvvm.ui.adapter.VideoListAdapter;
import com.release.mvvm.ui.page.video_page.VideoListFragment;
import com.trello.rxlifecycle3.components.support.RxFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/22
 * @Describe
 */
@Module(includes = {BaseChildFragmentModule.class, VideoListPresenterModule.class})
abstract class VideoListFragmentModule {

    @Binds
    @ChildFragment
    @ChildFragmentScope
    abstract RxFragment fragment(VideoListFragment videoListFragment);

//    @Binds
//    @ChildFragmentScope
//    abstract VideoListView videoListView(VideoListFragment videoListFragment);

    @Provides
    @ChildFragmentScope
    static VideoListAdapter videoListAdapter() {
        return new VideoListAdapter(R.layout.adapter_video_list, null);
    }
}
