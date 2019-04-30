package com.release.mvvm.ui.base;

/**
 * @author Mr.release
 * @create 2019/4/26
 * @Describe
 */
public interface IModel {
    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。
     */
    void onCleared();
}
