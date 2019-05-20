package com.release.base.base;

import android.os.Bundle;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
interface UIIterfaceAct {

    int getLayoutId(Bundle savedInstanceState);

    int initVariableId();

    void initView();

    void initListener();

    void updateViews(boolean isRefresh);
}
