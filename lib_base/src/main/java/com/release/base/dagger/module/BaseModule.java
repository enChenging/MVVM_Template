//package com.release.base.dagger.module;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.release.base.utils.SPUtil;
//import com.release.base.utils.baserx.RxBus;
//
//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;
//
///**
// * @author Mr.release
// * @create 2019/5/16
// * @Describe
// */
//@Module
//public class BaseModule {
//
//    public Application mContext;
//
//    public BaseModule(Application mContext) {
//        this.mContext = mContext;
//    }
//
//    @Provides
//    @Singleton
//    public Context provideContext() {
//        return mContext;
//    }
//
//    @Provides
//    @Singleton
//    public SPUtil provideSPUtil() {
//        return SPUtil.getInstance(mContext);
//    }
//
//    @Provides
//    @Singleton
//    public RxBus rxBus() {
//        return new RxBus();
//    }
//}
