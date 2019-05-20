package com.release.mvvm.ui.page.recommend_page;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.release.base.base.BaseViewModel;
import com.release.base.base.SingleLiveEvent;
import com.release.base.utils.baserx.CommonSubscriber;
import com.release.base.utils.baserx.RxUtil;
import com.release.mvvm.bean.RecommendPageBean;
import com.release.mvvm.http.RetrofitHelper;
import com.release.mvvm.ui.web_detail.WebDetailActivity;
import com.release.mvvm.utils.Constants;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class RecommendViewModel extends BaseViewModel {


    public SingleLiveEvent<List<RecommendPageBean.NewslistBean>> finishLoadData = new SingleLiveEvent();

    public RecommendViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void loadData(RecommendPage recommendPage) {
        addSubscribe(
                RetrofitHelper
                        .getRecommendData("4a0090627cf07a50def18da875165740", 20)
                        .doOnSubscribe(subscription -> showLoading())
                        .as(RxUtil.bindLifecycle(recommendPage))
                        .subscribeWith(new CommonSubscriber<RecommendPageBean>() {
                            @Override
                            protected void _onNext(RecommendPageBean recommendPageBean) {
                                finishLoadData.setValue(recommendPageBean.getNewslist());
                            }

                            @Override
                            protected void _onError(String message) {
                                showNetError();
                            }

                            @Override
                            protected void _onComplete() {
                                hideLoading();
                            }
                        }));

    }

    public void goToWebDetail(RecommendPageBean.NewslistBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECOMMEND_BUNDLE, bean);
        startActivity(WebDetailActivity.class, bundle);
    }
}
