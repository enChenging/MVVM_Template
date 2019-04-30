package com.release.mvvm;

import android.app.Application;

import com.release.mvvm.dao.DaoMaster;
import com.release.mvvm.dao.DaoSession;
import com.release.mvvm.injector.base.ActivityScope;
import com.release.mvvm.injector.guide_module.GuideActivityModule;
import com.release.mvvm.injector.main_module.MainActivityModule;
import com.release.mvvm.injector.news_detail_module.NewsDetailActivityModule;
import com.release.mvvm.injector.news_special_module.NewsSpecialActivityModule;
import com.release.mvvm.injector.photo_album_module.PhotoAlbumActivityModule;
import com.release.mvvm.injector.splash_module.SplashActivityModule;
import com.release.mvvm.injector.web_detail_module.WebDetailActivityModule;
import com.release.mvvm.ui.guide.GuideActivity;
import com.release.mvvm.ui.home.MainActivity;
import com.release.mvvm.ui.page.news_page.NewsDetailActivity;
import com.release.mvvm.ui.page.news_page.NewsSpecialActivity;
import com.release.mvvm.ui.page.news_page.PhotoAlbumActivity;
import com.release.mvvm.ui.web_detail.WebDetailActivity;
import com.release.mvvm.ui.splash.SplashActivity;
import com.release.mvvm.utils.baserx.RxBus;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Mr.release
 * @create 2019/4/18
 * @Describe
 */
@Module(includes = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class})
abstract class AppModule {

    @Binds
    @Singleton
    abstract Application application(App app);

    @Provides
    @Singleton
    static RxBus rxBus() {
        return new RxBus();
    }

    @Provides
    @Singleton
    static DaoSession daoSession(App app) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, "dao_db");
        Database database = helper.getWritableDb();
        return new DaoMaster(database).newSession();
    }

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity splashActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = GuideActivityModule.class)
    abstract GuideActivity guideActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = NewsDetailActivityModule.class)
    abstract NewsDetailActivity newsDetailActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = NewsSpecialActivityModule.class)
    abstract NewsSpecialActivity newsSpecialActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = PhotoAlbumActivityModule.class)
    abstract PhotoAlbumActivity photoAlbumActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = WebDetailActivityModule.class)
    abstract WebDetailActivity eventDetailActivityInjector();
}
