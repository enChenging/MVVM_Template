package com.release.mvvm.ui.page.recommend_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.release.base.base.BaseFragment;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.bean.RecommendPageBean;
import com.release.mvvm.databinding.PageRecommendBinding;
import com.release.mvvm.ui.adapter.RecommendAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class RecommendPage extends BaseFragment<PageRecommendBinding, RecommendViewModel> {
    private static final String TAG = RecommendPage.class.getSimpleName();

    @Inject
    RecommendAdapter mAdapter;

    @Override
    public int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.page_recommend;
    }

    @Override
    public void initView(View view) {


        binding.rvList.setHasFixedSize(true);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvList.setAdapter(mAdapter);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initListener() {

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                RecommendPageBean.NewslistBean bean = (RecommendPageBean.NewslistBean) adapter.getData().get(position);
                viewModel.goToWebDetail(bean);
            }
        });


        viewModel.finishLoadData.observe(this, new Observer<List<RecommendPageBean.NewslistBean>>() {
            @Override
            public void onChanged(List<RecommendPageBean.NewslistBean> newslistBeans) {
                mAdapter.setNewData(newslistBeans);
            }
        });
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.loadData(this);
    }
}
