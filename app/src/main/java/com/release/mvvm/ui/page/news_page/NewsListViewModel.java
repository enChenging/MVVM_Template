package com.release.mvvm.ui.page.news_page;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

import com.release.mvvm.bean.NewsInfoBean;
import com.release.mvvm.http.RetrofitHelper;
import com.release.mvvm.ui.adapter.item.NewsMultiItem;
import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.ui.base.SingleLiveEvent;
import com.release.mvvm.utils.LogUtils;
import com.release.mvvm.utils.NewsUtils;
import com.release.mvvm.utils.ToastUtils;
import com.release.mvvm.utils.baserx.CommonSubscriber;
import com.release.mvvm.utils.baserx.RxUtil;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author Mr.release
 * @create 2019/4/30
 * @Describe
 */
public class NewsListViewModel extends BaseViewModel {
    private static final String TAG = NewsListViewModel.class.getSimpleName();
    private int mPage = 0;
    public SingleLiveEvent<NewsInfoBean> finishLoadAdData = new SingleLiveEvent();
    public SingleLiveEvent<List<NewsMultiItem>> finishLoadData = new SingleLiveEvent();
    public SingleLiveEvent<List<NewsMultiItem>> finishLoadMoreData = new SingleLiveEvent();
    public SingleLiveEvent finishNoData = new SingleLiveEvent();


    public NewsListViewModel(@NonNull Application application) {
        super(application);
    }


    @SuppressLint("CheckResult")
    public void loadData(boolean isRefresh, String newsId) {

        LogUtils.i(TAG, "loadData---mNewsId: " + newsId);

        RetrofitHelper
                .getImportantNewAPI(newsId, mPage)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        if (!isRefresh) {
                            showLoading();
                        }
                    }
                })
                .filter(new Predicate<NewsInfoBean>() {
                    @Override
                    public boolean test(NewsInfoBean newsInfo) throws Exception {
                        if (NewsUtils.isAbNews(newsInfo))
                            finishLoadAdData.setValue(newsInfo);
                        return !NewsUtils.isAbNews(newsInfo);
                    }
                })
                .compose(observableTransformer)
                .subscribeWith(new CommonSubscriber<List<NewsMultiItem>>() {
                    @Override
                    protected void _onNext(List<NewsMultiItem> newsMultiItems) {
                        LogUtils.d(TAG, "_onNext: ");
                        finishLoadData.setValue(newsMultiItems);
                        mPage++;
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtils.d(TAG, "_onError: " + message);
                        if (isRefresh) {
                            finishRefresh();
                            ToastUtils.show("刷新失败");
                        } else {
                            showNetError();
                        }
                    }

                    @Override
                    protected void _onComplete() {
                        LogUtils.d(TAG, "_onComplete: ");
                        if (isRefresh) {
                            finishRefresh();
                        } else {
                            hideLoading();
                        }
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void loadMoreData() {
        LogUtils.i(TAG, "loadMoreData: ");
        RetrofitHelper
                .getImportantNewAPI("T1348647909107", mPage)
                .compose(observableTransformer)
                .subscribeWith(new CommonSubscriber<List<NewsMultiItem>>() {
                    @Override
                    protected void _onNext(List<NewsMultiItem> newsMultiItems) {
                        LogUtils.d(TAG, "_onNext: ");
                        finishLoadMoreData.setValue(newsMultiItems);
                        mPage++;
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtils.d(TAG, "_onError: " + message);
                        finishNoData.call();
                    }

                    @Override
                    protected void _onComplete() {
                        LogUtils.d(TAG, "_onComplete: ");
                    }
                });
    }


    private FlowableTransformer<NewsInfoBean, List<NewsMultiItem>> observableTransformer = new FlowableTransformer<NewsInfoBean, List<NewsMultiItem>>() {

        @Override
        public Publisher<List<NewsMultiItem>> apply(Flowable<NewsInfoBean> upstream) {

            return upstream
                    .map(new Function<NewsInfoBean, NewsMultiItem>() {
                        @Override
                        public NewsMultiItem apply(NewsInfoBean newsInfo) throws Exception {

                            if (NewsUtils.isNewsPhotoSet(newsInfo.getSkipType())) {
                                return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_PHOTO_SET, newsInfo);
                            }
                            return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_NORMAL, newsInfo);
                        }
                    })
                    .toList()
                    .toFlowable()
                    .compose(RxUtil.bindToLifecycle(getLifecycleProvider()));
        }
    };
}
