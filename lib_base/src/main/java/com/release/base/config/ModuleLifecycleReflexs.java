package com.release.base.config;

/**
 * 组件生命周期反射类名管理，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 * 注意：以下模块中初始化的Module类不能被混淆
 */

public class ModuleLifecycleReflexs {
    private static final String BaseInit = "com.release.base.base.BaseModuleInit";

    //登录模块
    private static final String LoginInit = "com.release.login.LoginModuleInit";

    public static String[] initModuleNames = {BaseInit, LoginInit};
}
