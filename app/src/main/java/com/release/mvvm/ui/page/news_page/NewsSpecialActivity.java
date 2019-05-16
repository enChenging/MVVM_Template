package com.release.mvvm.ui.page.news_page;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.release.base.base.BaseActivity;
import com.release.base.utils.AnimateHelper;
import com.release.base.utils.DefIconFactory;
import com.release.base.utils.ImageLoader;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.bean.SpecialInfoBean;
import com.release.mvvm.databinding.ActivitySpecialBinding;
import com.release.mvvm.ui.adapter.NewsSpecialAdapter;
import com.release.mvvm.ui.adapter.item.SpecialItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.release.mvvm.utils.Constants.SPECIAL_KEY;

/**
 * 新闻专题
 *
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsSpecialActivity extends BaseActivity<ActivitySpecialBinding, NewsSpecialViewModel> {

    private static final String TAG = NewsSpecialActivity.class.getSimpleName();
    @Inject
    NewsSpecialAdapter mAdapter;


    private final int[] mSkipId = new int[20];
    private TagLayout mTagLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Animator mTopBarAnimator;
    private String mSpecialId;


    public static void start(Context context, String newsId) {
        Intent intent = new Intent(context, NewsSpecialActivity.class);
        intent.putExtra(SPECIAL_KEY, newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }


    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_special;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        binding.toolBar.setBackgroundColor(getResources().getColor(R.color.white));
        mSpecialId = getIntent().getStringExtra(SPECIAL_KEY);

        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        binding.rvNewsList.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        binding.rvNewsList.setLayoutManager(mLinearLayoutManager);
        binding.rvNewsList.setAdapter(mAdapter);

    }

    boolean change;

    @Override
    public void initListener() {

        int topBarHeight = getResources().getDimensionPixelSize(R.dimen.default_toolbar_height);

        mTopBarAnimator = AnimateHelper.doMoveVertical(binding.toolBar, (int) binding.toolBar.getTranslationY(), -topBarHeight, 0);

        binding.rvNewsList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0 && !change) {
                    if (AnimateHelper.isRunning(mTopBarAnimator))
                        return;
                    mTopBarAnimator = AnimateHelper.doMoveVertical(binding.toolBar, (int) binding.toolBar.getTranslationY(), 0, 300);
                    change = true;
                } else if (dy > 0 && change) {
                    AnimateHelper.stopAnimator(mTopBarAnimator);
                    ViewCompat.setTranslationY(binding.toolBar, -topBarHeight);
                    change = false;
                }
            }
        });

        viewModel.fabCoping.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                mLinearLayoutManager.scrollToPosition(0);
            }
        });

        viewModel.finishHeadData.observe(this, new Observer<SpecialInfoBean>() {
            @Override
            public void onChanged(SpecialInfoBean specialInfoBean) {
                loadHeadView(specialInfoBean);
            }
        });

        viewModel.finishLoadData.observe(this, new Observer<List<SpecialItem>>() {
            @Override
            public void onChanged(List<SpecialItem> specialItems) {
                mAdapter.setNewData(specialItems);
                _handleTagLayout(specialItems);
            }
        });
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.loadData(mSpecialId);
    }


    public void loadHeadView(SpecialInfoBean specialBean) {
        View headView = LayoutInflater.from(this).inflate(R.layout.head_special, null);
        ImageView mIvBanner = headView.findViewById(R.id.iv_banner);

        ImageLoader.loadFitCenter(this, specialBean.getBanner(), mIvBanner, DefIconFactory.provideIcon());

        // 添加导语
        if (!TextUtils.isEmpty(specialBean.getDigest())) {
            ViewStub stub = headView.findViewById(R.id.vs_digest);
            assert stub != null;
            stub.inflate();
            TextView tvDigest = headView.findViewById(R.id.tv_digest);
            tvDigest.setText(specialBean.getDigest());
        }
        mTagLayout = headView.findViewById(R.id.tag_layout);
        mAdapter.addHeaderView(headView);
    }

    /**
     * 处理导航标签
     *
     * @param specialItems
     */
    @SuppressLint("CheckResult")
    private void _handleTagLayout(List<SpecialItem> specialItems) {
        Observable.fromIterable(specialItems)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<SpecialItem>() {
                    int i = 0;
                    int index = mAdapter.getHeaderViewsCount();  // 存在一个 HeadView 所以从1算起

                    @Override
                    public boolean test(SpecialItem specialItem) throws Exception {

                        if (specialItem.isHeader) {
                            // 记录头部的列表索引值，用来跳转
                            mSkipId[i++] = index;
                        }
                        index++;
                        return specialItem.isHeader;
                    }
                })
                .map(new Function<SpecialItem, String>() {
                    @Override
                    public String apply(SpecialItem specialItem) throws Exception {
                        return _clipHeadStr(specialItem.header);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mTagLayout.addTag(s);
                    }
                });

        mTagLayout.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text, @TagView.TagMode int tagMode) {
                // 跳转到对应position,比scrollToPosition（）精确
                mLinearLayoutManager.scrollToPositionWithOffset(mSkipId[position], 0);
            }
        });
    }

    private String _clipHeadStr(String headStr) {
        String head = null;
        int index = headStr.indexOf(" ");
        if (index != -1) {
            head = headStr.substring(index, headStr.length());
        }
        return head;
    }

}
