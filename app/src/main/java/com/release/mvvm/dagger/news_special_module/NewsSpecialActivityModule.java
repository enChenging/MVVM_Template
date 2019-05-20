package com.release.mvvm.dagger.news_special_module;

import androidx.appcompat.app.AppCompatActivity;

import com.release.base.dagger.module.BaseActivityModule;
import com.release.base.dagger.scope.ActivityScope;
import com.release.mvvm.R;
import com.release.mvvm.ui.adapter.NewsSpecialAdapter;
import com.release.mvvm.ui.page.news_page.NewsSpecialActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/20
 * @Describe
 */

@Module(includes = {BaseActivityModule.class})
public abstract class NewsSpecialActivityModule {

    @Binds
    @ActivityScope
    abstract AppCompatActivity activity(NewsSpecialActivity activity);

    @Provides
    @ActivityScope
    static NewsSpecialAdapter newsSpecialAdapter() {
        return new NewsSpecialAdapter(R.layout.adapter_news_list, R.layout.adapter_special_head, null);
    }
}
