package com.release.mvvm.ui.page.news_page;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;

import com.release.base.base.BaseActivity;
import com.release.base.utils.AnimateHelper;
import com.release.base.utils.ListUtils;
import com.release.base.widget.EmptyLayout;
import com.release.base.widget.PullScrollView;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.bean.NewsDetailInfoBean;
import com.release.mvvm.databinding.FragmentNewsDetailBinding;
import com.release.mvvm.utils.NewsUtils;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import java.util.List;

import static com.release.mvvm.utils.Constants.NEWS_ID_KEY;

/**
 * 新闻详情
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsDetailActivity extends BaseActivity<FragmentNewsDetailBinding,NewsDetailViewModel> {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private int mMinScrollSlop;
    private Animator mTopBarAnimator;
    private int mLastScrollY = 0;
    private String mNextNewsId;
    private String mNewsId;


    public static void start(Context context, String newsId) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    private void startInside(String newsId) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return  R.layout.fragment_news_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        mNewsId = getIntent().getStringExtra(NEWS_ID_KEY);
        RichText.initCacheDir(this);
        RichText.debugMode = false;
        mMinScrollSlop = ViewConfiguration.get(this).getScaledTouchSlop(); // 最小触摸滑动距离
    }


    @Override
    public void initListener() {
        int topBarHeight = getResources().getDimensionPixelSize(R.dimen.default_toolbar_height);

        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > topBarHeight) {
                    if (AnimateHelper.isRunning(mTopBarAnimator)) {
                        return;
                    }
                    if (Math.abs(scrollY - mLastScrollY) > mMinScrollSlop) {
                        boolean isPullUp = scrollY > mLastScrollY;
                        mLastScrollY = scrollY;
                        if (isPullUp && binding.llTopBar2.getTranslationY() != -topBarHeight) {
                            mTopBarAnimator = AnimateHelper.doMoveVertical(binding.llTopBar2, (int) binding.llTopBar2.getTranslationY(),
                                    -topBarHeight, 300);
                        } else if (!isPullUp && binding.llTopBar2.getTranslationY() != 0) {
                            mTopBarAnimator = AnimateHelper.doMoveVertical(binding.llTopBar2, (int) binding.llTopBar2.getTranslationY(),
                                    0, 300);
                        }
                    }
                } else {
                    if (binding.llTopBar2.getTranslationY() != -topBarHeight) {
                        AnimateHelper.stopAnimator(mTopBarAnimator);
                        ViewCompat.setTranslationY(binding.llTopBar2, -topBarHeight);
                        mLastScrollY = 0;
                    }
                }
            }
        });
        binding.scrollView.setFootView(binding.includeFoot.llFootView);
        binding.scrollView.setPullListener(new PullScrollView.OnPullListener() {

            @Override
            public boolean isDoPull() {
                if (mEmptyLayout.getEmptyStatus() != EmptyLayout.STATUS_HIDE) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean handlePull() {
                if (TextUtils.isEmpty(mNextNewsId)) {
                    return false;
                } else {
                    startInside(mNextNewsId);
                    return true;
                }
            }
        });

        viewModel.finishLoadData.observe(this, new Observer<NewsDetailInfoBean>() {
            @Override
            public void onChanged(NewsDetailInfoBean newsDetailInfoBean) {
                binding.setBeanModel(newsDetailInfoBean);

                RichText.from(newsDetailInfoBean.getBody()).into(binding.includeNewsContent.tvContent);
                _handleSpInfo(newsDetailInfoBean.getSpinfo());
                _handleRelatedNews(newsDetailInfoBean);
            }
        });

        viewModel.fab.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.loadData(mNewsId);
    }

    /**
     * 处理关联的内容
     *
     * @param spinfo
     */
    private void _handleSpInfo(List<NewsDetailInfoBean.SpinfoBean> spinfo) {
        if (!ListUtils.isEmpty(spinfo)) {
            ViewStub stub = findViewById(R.id.vs_related_content);
            assert stub != null;//断言
            stub.inflate();
            TextView tvType = findViewById(R.id.tv_type);
            TextView tvRelatedContent = findViewById(R.id.tv_related_content);
            tvType.setText(spinfo.get(0).getSptype());
            RichText.from(spinfo.get(0).getSpcontent())
                    // 这里处理超链接的点击
                    .urlClick(new OnUrlClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            String newsId = NewsUtils.clipNewsIdFromUrl(url);
                            if (newsId != null) {
                                mNewsId = newsId;
                                onRetry();
                            }
                            return true;
                        }
                    })
                    .into(tvRelatedContent);
        }
    }

    /**
     * 处理关联新闻
     *
     * @param newsDetailBean
     */
    private void _handleRelatedNews(NewsDetailInfoBean newsDetailBean) {
        if (ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            binding.includeFoot.tvNextTitle.setText("没有相关文章了");
        } else {
            mNextNewsId = newsDetailBean.getRelative_sys().get(0).getId();
            binding.includeFoot.tvNextTitle.setText(newsDetailBean.getRelative_sys().get(0).getTitle());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
    }

}
