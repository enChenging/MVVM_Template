package com.release.mvvm.ui.page.video_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.release.base.base.BaseFragment;
import com.release.base.base.ViewPagerAdapter;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.databinding.PageVideoBinding;

import javax.inject.Inject;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class VideoPage extends BaseFragment<PageVideoBinding, VideoViewModel> {

    @Inject
    ViewPagerAdapter mAdapter;

    @Override
    public int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.page_video;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.initData();
        loadDataView();
    }


    public void loadDataView() {
        mAdapter.setItems(viewModel.mFragments, viewModel.VIDEO_TITLE);
        binding.viewPager.setAdapter(mAdapter);
        binding.stlTabLayout.setViewPager(binding.viewPager);
    }
}
