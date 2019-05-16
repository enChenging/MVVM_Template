package com.release.mvvm.ui.page.news_page;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.release.base.base.BaseViewModel;
import com.release.base.base.SingleLiveEvent;
import com.release.base.binding.command.BindingAction;
import com.release.base.binding.command.BindingCommand;
import com.release.base.utils.LogUtils;
import com.release.base.utils.baserx.CommonSubscriber;
import com.release.base.utils.baserx.RxUtil;
import com.release.mvvm.bean.NewsItemInfoBean;
import com.release.mvvm.bean.SpecialInfoBean;
import com.release.mvvm.http.RetrofitHelper;
import com.release.mvvm.ui.adapter.item.SpecialItem;

import org.reactivestreams.Publisher;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Mr.release
 * @create 2019/4/30
 * @Describe
 */
public class NewsSpecialViewModel extends BaseViewModel {
    private static final String TAG = NewsSpecialViewModel.class.getSimpleName();


    public SingleLiveEvent fabCoping = new SingleLiveEvent();
    public SingleLiveEvent<SpecialInfoBean> finishHeadData = new SingleLiveEvent();
    public SingleLiveEvent<List<SpecialItem>> finishLoadData = new SingleLiveEvent();

    public NewsSpecialViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand fabClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            fabCoping.call();
        }
    });


    @SuppressLint("CheckResult")
    public void loadData(String specialId) {
        RetrofitHelper
                .getNewsSpecialAPI(specialId)
                .doOnSubscribe(subscription -> hideLoading())
                .flatMap(new Function<SpecialInfoBean, Publisher<SpecialItem>>() {
                    @Override
                    public Publisher<SpecialItem> apply(SpecialInfoBean specialInfoBean) throws Exception {
                        String s = JSON.toJSONString(specialInfoBean);
                        LogUtils.i(TAG, "SpecialInfoBean: " + s);
                        finishHeadData.setValue(specialInfoBean);
                        return _convertSpecialBeanToItem(specialInfoBean);
                    }
                })
                .toList()
                .toFlowable()
                .compose(RxUtil.bindToLifecycle(getLifecycleProvider()))
                .subscribeWith(new CommonSubscriber<List<SpecialItem>>() {
                    @Override
                    protected void _onNext(List<SpecialItem> specialItems) {
                        LogUtils.i(TAG, "_onNext: ");
                        finishLoadData.setValue(specialItems);
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtils.i(TAG, "_onError: ");
                        showNetError();
                    }

                    @Override
                    protected void _onComplete() {
                        LogUtils.i(TAG, "_onComplete: ");
                        hideLoading();
                    }
                });
    }

    private Flowable<SpecialItem> _convertSpecialBeanToItem(SpecialInfoBean specialBean) {
        // 这边 +1 是接口数据还有个 topicsplus 的字段可能是穿插在 topics 字段列表中间。这里没有处理 topicsplus
        final SpecialItem[] specialItems = new SpecialItem[specialBean.getTopics().size() + 1];


        return Flowable.fromIterable(specialBean.getTopics())
                // 获取头部
                .doOnNext(new Consumer<SpecialInfoBean.TopicsBean>() {
                    @Override
                    public void accept(SpecialInfoBean.TopicsBean topicsEntity) throws Exception {
                        specialItems[topicsEntity.getIndex() - 1] = new SpecialItem(true,
                                topicsEntity.getIndex() + "/" + specialItems.length + " " + topicsEntity.getTname());
                    }
                })
                // 排序
                .toSortedList(new Comparator<SpecialInfoBean.TopicsBean>() {
                    @Override
                    public int compare(SpecialInfoBean.TopicsBean o1, SpecialInfoBean.TopicsBean o2) {
                        return o1.getIndex() - o2.getIndex();
                    }
                })
                .toFlowable()
                // 拆分
                .flatMap(new Function<List<SpecialInfoBean.TopicsBean>, Publisher<SpecialInfoBean.TopicsBean>>() {
                    @Override
                    public Publisher<SpecialInfoBean.TopicsBean> apply(List<SpecialInfoBean.TopicsBean> topicsEntities) throws Exception {
                        return Flowable.fromIterable(topicsEntities);
                    }
                })
                .flatMap(new Function<SpecialInfoBean.TopicsBean, Publisher<SpecialItem>>() {
                    @Override
                    public Publisher<SpecialItem> apply(SpecialInfoBean.TopicsBean topicsEntity) throws Exception {
                        // 转换并在每个列表项增加头部
                        return Flowable.fromIterable(topicsEntity.getDocs())
                                .map(new Function<NewsItemInfoBean, SpecialItem>() {
                                    @Override
                                    public SpecialItem apply(NewsItemInfoBean newsItemInfoBean) throws Exception {
                                        return new SpecialItem(newsItemInfoBean);
                                    }
                                })
                                .startWith(specialItems[topicsEntity.getIndex() - 1]);
                    }
                });
    }
}
