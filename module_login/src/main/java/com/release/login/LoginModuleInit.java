package com.release.login;

import android.app.Application;

import com.release.base.base.IModuleInit;


/**
 * Created by goldze on 2018/6/21 0021.
 */

public class LoginModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
//        KLog.e("登录模块初始化 -- onInitAhead");
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
//        KLog.e("登录模块初始化 -- onInitLow");
        return false;
    }
}
