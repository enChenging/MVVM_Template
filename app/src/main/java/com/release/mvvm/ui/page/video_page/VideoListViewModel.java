package com.release.mvvm.ui.page.video_page;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

import com.release.mvvm.dao.VideoInfo;
import com.release.mvvm.http.RetrofitHelper;
import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.ui.base.SingleLiveEvent;
import com.release.mvvm.utils.LogUtils;
import com.release.mvvm.utils.ToastUtils;
import com.release.mvvm.utils.baserx.CommonSubscriber;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class VideoListViewModel extends BaseViewModel {

    private static final String TAG = VideoListViewModel.class.getSimpleName();
    private int mPage = 0;
    public SingleLiveEvent<List<VideoInfo>> finishLoadData = new SingleLiveEvent();
    public SingleLiveEvent<List<VideoInfo>> finishLoadMoreData = new SingleLiveEvent();
    public SingleLiveEvent finishNoData = new SingleLiveEvent();


    public VideoListViewModel(@NonNull Application application) {
        super(application);
    }


    @SuppressLint("CheckResult")
    public void loadData(boolean isRefresh, String videoId) {

        addSubscribe(
                RetrofitHelper
                        .getVideoListAPI(videoId, mPage)
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                if (!isRefresh) {
                                    showLoading();
                                }
                            }
                        })
                        .subscribeWith(new CommonSubscriber<List<VideoInfo>>() {
                            @Override
                            protected void _onNext(List<VideoInfo> videoInfos) {
                                LogUtils.i(TAG, "accept: " + videoInfos);
                                finishLoadData.setValue(videoInfos);
                                mPage++;
                            }

                            @Override
                            protected void _onError(String message) {
                                if (isRefresh) {
                                    finishRefresh();
                                    ToastUtils.show("刷新失败");
                                } else {
                                    showNetError();
                                }
                            }

                            @Override
                            protected void _onComplete() {
                                LogUtils.i(TAG, "run: ");
                                if (isRefresh) {
                                    finishRefresh();
                                } else {
                                    hideLoading();
                                }
                            }
                        }));
    }

    @SuppressLint("CheckResult")
    public void loadMoreData(String videoId) {

        addSubscribe(
                RetrofitHelper
                        .getVideoListAPI(videoId, mPage)
                        .subscribeWith(new CommonSubscriber<List<VideoInfo>>() {
                            @Override
                            protected void _onNext(List<VideoInfo> videoInfos) {
                                LogUtils.i(TAG, "loadMoreData---accept: " + videoInfos);
                                finishLoadMoreData.setValue(videoInfos);
                                mPage++;

                            }

                            @Override
                            protected void _onError(String message) {
                                LogUtils.e(TAG, "loadMoreData---throwable: " + message);
                                finishNoData.call();
                            }

                            @Override
                            protected void _onComplete() {
                                LogUtils.i(TAG, "loadMoreData---run: ");
                            }
                        }));
    }
}
