package com.release.mvvm.dagger.main_module.video_page;

import com.release.base.dagger.module.BaseChildFragmentModule;
import com.release.base.dagger.qualifiers.ChildFragment;
import com.release.base.dagger.scope.ChildFragmentScope;
import com.release.mvvm.R;
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
@Module(includes = {BaseChildFragmentModule.class})
abstract class VideoListFragmentModule {

    @Binds
    @ChildFragment
    @ChildFragmentScope
    abstract RxFragment fragment(VideoListFragment videoListFragment);

    @Provides
    @ChildFragmentScope
    static VideoListAdapter videoListAdapter() {
        return new VideoListAdapter(R.layout.adapter_video_list, null);
    }
}
