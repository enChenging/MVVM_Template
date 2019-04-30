package com.release.mvvm.injector.news_special_module;

import com.release.mvvm.R;
import com.release.mvvm.injector.base.ActivityScope;
import com.release.mvvm.injector.base.BaseActivityModule;
import com.release.mvvm.ui.adapter.NewsSpecialAdapter;
import com.release.mvvm.ui.page.news_page.NewsSpecialActivity;
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
public abstract class NewsSpecialActivityModule {

    @Binds
    @ActivityScope
    abstract RxAppCompatActivity activity(NewsSpecialActivity activity);

    @Provides
    @ActivityScope
    static NewsSpecialAdapter newsSpecialAdapter() {
        return new NewsSpecialAdapter(R.layout.adapter_news_list, R.layout.adapter_special_head, null);
    }
}
