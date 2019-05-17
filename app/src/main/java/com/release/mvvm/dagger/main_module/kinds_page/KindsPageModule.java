package com.release.mvvm.dagger.main_module.kinds_page;

import com.release.base.dagger.module.BaseFragmentModule;
import com.release.base.dagger.qualifiers.Fragment;
import com.release.base.dagger.scope.FragmentScope;
import com.release.mvvm.ui.page.kinds_page.KindsPage;
import com.trello.rxlifecycle3.components.support.RxFragment;

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
    @Fragment
    @FragmentScope
    abstract RxFragment fragment(KindsPage kindsPage);

}
