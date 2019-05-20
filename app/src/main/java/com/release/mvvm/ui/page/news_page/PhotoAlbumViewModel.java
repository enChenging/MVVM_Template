package com.release.mvvm.ui.page.news_page;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.release.base.base.BaseViewModel;
import com.release.base.base.SingleLiveEvent;
import com.release.base.utils.LogUtils;
import com.release.base.utils.baserx.CommonSubscriber;
import com.release.base.utils.baserx.RxUtil;
import com.release.mvvm.bean.PhotoSetInfoBean;
import com.release.mvvm.http.RetrofitHelper;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.reactivestreams.Subscription;

import io.reactivex.functions.Consumer;

/**
 * @author Mr.release
 * @create 2019/4/30
 * @Describe
 */
public class PhotoAlbumViewModel extends BaseViewModel {
    private static final String TAG = PhotoAlbumViewModel.class.getSimpleName();

    public SingleLiveEvent<PhotoSetInfoBean> finishLoadData = new SingleLiveEvent();

    public PhotoAlbumViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void loadData(PhotoAlbumActivity photoAlbumActivity, String photoSetId) {
        LogUtils.i(TAG, "loadData---mPhotoSetId: " + photoSetId);
        RetrofitHelper
                .getPhotoAlbumAPI(photoSetId)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        showLoading();
                    }
                })
                .as(RxUtil.bindLifecycle(photoAlbumActivity))
                .subscribeWith(new CommonSubscriber<PhotoSetInfoBean>() {
                    @Override
                    protected void _onNext(PhotoSetInfoBean photoSetInfoBean) {
                        LogUtils.i(TAG, "_onNext: " + photoSetInfoBean);
                        finishLoadData.setValue(photoSetInfoBean);
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtils.i(TAG, "throwable: " + message);
                        showNetError();
                    }

                    @Override
                    protected void _onComplete() {
                        hideLoading();
                    }
                });
    }

}
