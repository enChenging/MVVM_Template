package com.release.mvvm.ui.web_detail;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.release.base.base.BaseViewModel;
import com.release.mvvm.bean.RecommendPageBean;

import static com.release.mvvm.utils.Constants.RECOMMEND_BUNDLE;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class WebDetailViewModel extends BaseViewModel {

    public ObservableField<String> tvTitleContent = new ObservableField<>();
    public ObservableField<String> tvSource = new ObservableField<>();
    public ObservableField<String> tvTime = new ObservableField<>();

    public WebDetailViewModel(@NonNull Application application) {
        super(application);
    }


    public String setIntentData(Intent intent) {

        Bundle bundle = intent.getExtras();

        RecommendPageBean.NewslistBean bean = (RecommendPageBean.NewslistBean) bundle.getSerializable(RECOMMEND_BUNDLE);
        tvTitleContent.set(bean.getTitle());
        tvSource.set(bean.getDescription());
        tvTime.set(bean.getCtime());
        return bean.getUrl();
    }
}
