package com.release.mvvm.ui.page.news_page;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.release.base.base.BaseFragment;
import com.release.base.base.ViewPagerAdapter;
import com.release.base.utils.KeyboardUtils;
import com.release.base.utils.ToastUtils;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.dao.DaoSession;
import com.release.mvvm.dao.NewsTypeInfo;
import com.release.mvvm.databinding.PageNewsBinding;
import com.release.mvvm.ui.home.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class NewsPage extends BaseFragment<PageNewsBinding, NewsPageViewModel> {
    private static final String TAG = NewsPage.class.getSimpleName();
    @Inject
    ViewPagerAdapter mAdapter;
    @Inject
    DaoSession mDaoSession;

    @Override
    public int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.page_news;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initListener() {

        viewModel.setting.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                ((MainActivity)getActivity()).toggle();
            }
        });

        viewModel.finishLoadData.observe(this, new Observer<List<NewsTypeInfo>>() {
            @Override
            public void onChanged(List<NewsTypeInfo> newsTypeInfos) {
                List<Fragment> fragments = new ArrayList<>();
                List<String> titles = new ArrayList<>();

                for (NewsTypeInfo bean : newsTypeInfos) {
                    titles.add(bean.getName());
                    fragments.add(NewsListFragment.newInstance(bean.getTypeId()));
                }

                mAdapter.setItems(fragments, titles);
                binding.viewPager.setAdapter(mAdapter);
                binding.stlTabLayout.setViewPager(binding.viewPager);
            }
        });


        binding.includeTop.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideKeyboard(binding.includeTop.editText);
                    if (!TextUtils.isEmpty(v.getText()))
                        ToastUtils.show(v.getText().toString());
                    else
                        ToastUtils.show("请输入搜索内容");
                }
                return false;
            }
        });
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.loadData(mDaoSession);
    }
}
