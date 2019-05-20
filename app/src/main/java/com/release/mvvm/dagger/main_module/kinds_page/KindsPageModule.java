package com.release.mvvm.dagger.main_module.kinds_page;

import androidx.fragment.app.Fragment;

import com.release.base.dagger.module.BaseFragmentModule;
import com.release.base.dagger.qualifiers.Fragmentq;
import com.release.base.dagger.scope.FragmentScope;
import com.release.mvvm.ui.page.kinds_page.KindsPage;

import dagger.Binds;
import dagger.Module;

/**
 * @author Mr.release
 * @create 2019/4/22
 * @Describe
 */
@Module(includes = {BaseFragmentModule.class})
public abstract class KindsPageModule {

    @Binds
    @Fragmentq
    @FragmentScope
    abstract Fragment fragment(KindsPage kindsPage);

}
