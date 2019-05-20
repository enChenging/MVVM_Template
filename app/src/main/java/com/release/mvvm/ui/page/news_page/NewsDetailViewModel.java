package com.release.mvvm.ui.page.news_page;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.fastjson.JSON;
import com.release.base.base.BaseViewModel;
import com.release.base.base.SingleLiveEvent;
import com.release.base.binding.command.BindingAction;
import com.release.base.binding.command.BindingCommand;
import com.release.base.utils.ListUtils;
import com.release.base.utils.LogUtils;
import com.release.base.utils.baserx.CommonSubscriber;
import com.release.base.utils.baserx.RxUtil;
import com.release.mvvm.bean.NewsDetailInfoBean;
import com.release.mvvm.http.RetrofitHelper;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.reactivestreams.Subscription;

import io.reactivex.functions.Consumer;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class NewsDetailViewModel extends BaseViewModel {

    private static final String TAG = NewsDetailViewModel.class.getSimpleName();
    private static final String HTML_IMG_TEMPLATE = "<img src=\"http\" />";
    public SingleLiveEvent<NewsDetailInfoBean> finishLoadData = new SingleLiveEvent();
    public SingleLiveEvent fab = new SingleLiveEvent();


    public NewsDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand back2 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand fabCoping = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            fab.call();
        }
    });


    @SuppressLint("CheckResult")
    public void loadData(NewsDetailActivity newsDetailActivity, String newsId) {

        LogUtils.i(TAG, "loadData: " + newsId);
        RetrofitHelper
                .getNewsDetailAPI(newsId)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        showLoading();
                    }
                })
                .doOnNext(new Consumer<NewsDetailInfoBean>() {
                    @Override
                    public void accept(NewsDetailInfoBean newsDetailInfoBean) throws Exception {
                        String s = JSON.toJSONString(newsDetailInfoBean);
                        LogUtils.i(TAG, "accept: " + s);
                        _handleRichTextWithImg(newsDetailInfoBean);
                    }
                })
                .as(RxUtil.bindLifecycle(newsDetailActivity))
                .subscribeWith(new CommonSubscriber<NewsDetailInfoBean>() {
                    @Override
                    protected void _onNext(NewsDetailInfoBean newsDetailInfoBean) {
                        LogUtils.i(TAG, "_onNext: " + newsDetailInfoBean);
                        finishLoadData.setValue(newsDetailInfoBean);
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtils.i(TAG, "_onError: " + message);
                        showNetError();
                    }

                    @Override
                    protected void _onComplete() {
                        hideLoading();
                    }
                });
    }

    /**
     * 处理富文本包含图片的情况
     *
     * @param newsDetailBean 原始数据
     */
    private void _handleRichTextWithImg(NewsDetailInfoBean newsDetailBean) {
        if (!ListUtils.isEmpty(newsDetailBean.getImg())) {
            String body = newsDetailBean.getBody();
            for (NewsDetailInfoBean.ImgBean imgEntity : newsDetailBean.getImg()) {
                String ref = imgEntity.getRef();
                String src = imgEntity.getSrc();
                LogUtils.i(TAG, "imgEntity: " + imgEntity.toString());
                String img = HTML_IMG_TEMPLATE.replace("http", src);
                body = body.replaceAll(ref, img);
                LogUtils.i(TAG, "img: " + img);
                LogUtils.i(TAG, "body: " + body);
            }
            newsDetailBean.setBody(body);
        }
    }
}
