package com.release.base.dagger.module;


import androidx.fragment.app.FragmentManager;

import com.release.base.dagger.qualifiers.ChildFragmentManager;
import com.release.base.dagger.qualifiers.Fragmentq;
import com.release.base.dagger.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Mr.release
 * @create 2019/4/19
 * @Describe
 */
@Module
public abstract class BaseFragmentModule {

    @Provides
    @FragmentScope
    @ChildFragmentManager
    static FragmentManager childFragmentManager(@Fragmentq androidx.fragment.app.Fragment fragment) {
        return fragment.getChildFragmentManager();
    }

}
