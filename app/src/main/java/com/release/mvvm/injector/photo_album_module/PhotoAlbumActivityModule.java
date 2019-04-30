package com.release.mvvm.injector.photo_album_module;

import com.release.mvvm.injector.base.ActivityScope;
import com.release.mvvm.injector.base.BaseActivityModule;
import com.release.mvvm.ui.adapter.PhotoSetAdapter;
import com.release.mvvm.ui.page.news_page.PhotoAlbumActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */

@Module(includes = {BaseActivityModule.class})
public abstract class PhotoAlbumActivityModule {

    @Binds
    @ActivityScope
    abstract RxAppCompatActivity activity(PhotoAlbumActivity activity);

    @Provides
    @ActivityScope
    static PhotoSetAdapter photoSetAdapter(PhotoAlbumActivity activity) {
        return new PhotoSetAdapter(activity);
    }
}
