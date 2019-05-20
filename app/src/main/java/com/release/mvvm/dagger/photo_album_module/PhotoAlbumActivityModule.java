package com.release.mvvm.dagger.photo_album_module;

import androidx.appcompat.app.AppCompatActivity;

import com.release.base.dagger.module.BaseActivityModule;
import com.release.base.dagger.scope.ActivityScope;
import com.release.mvvm.ui.adapter.PhotoSetAdapter;
import com.release.mvvm.ui.page.news_page.PhotoAlbumActivity;

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
    abstract AppCompatActivity activity(PhotoAlbumActivity activity);

    @Provides
    @ActivityScope
    static PhotoSetAdapter photoSetAdapter(PhotoAlbumActivity activity) {
        return new PhotoSetAdapter(activity);
    }
}
