package com.release.mvvm.ui.page.kinds_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.release.base.base.BaseFragment;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.databinding.PageKindsBinding;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class KindsPage extends BaseFragment<PageKindsBinding, KindsViewModel> {

    public static KindsPage newInstance() {
        return new KindsPage();
    }

    @Override
    public int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.page_kinds;
    }


    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void updateViews(boolean isRefresh) {

    }


}
