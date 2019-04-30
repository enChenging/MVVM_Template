package com.release.mvvm.ui.page.recommend_page;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

import com.release.mvvm.bean.RecommendPageBean;
import com.release.mvvm.http.RetrofitHelper;
import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.ui.base.SingleLiveEvent;
import com.release.mvvm.utils.baserx.CommonSubscriber;

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
    public void loadData() {
        addSubscribe(
                RetrofitHelper
                        .getRecommendData("4a0090627cf07a50def18da875165740", 20)
                        .doOnSubscribe(subscription -> showLoading())
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
}
