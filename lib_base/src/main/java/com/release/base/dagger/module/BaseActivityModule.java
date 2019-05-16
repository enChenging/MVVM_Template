package com.release.base.dagger.module;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.release.base.dagger.qualifiers.ActivityFagmentManger;
import com.release.base.dagger.scope.ActivityScope;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/19
 * @Describe
 */
@Module
public abstract class BaseActivityModule {

    @Binds
    @ActivityScope
    abstract Context activityContext(RxAppCompatActivity activity);

    @Provides
    @ActivityScope
    @ActivityFagmentManger
    static FragmentManager activityFragmentManager(RxAppCompatActivity activity) {
        return activity.getSupportFragmentManager();
    }
}
