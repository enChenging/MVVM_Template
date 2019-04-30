package com.release.mvvm.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
interface UIIterfaceFrag {

    int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    int initVariableId();

    void initView(View view);

    void initListener();

    void updateViews(boolean isRefresh);

}
