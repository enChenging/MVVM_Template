package com.release.mvvm.ui.web_detail;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.release.base.base.BaseViewModel;
import com.release.mvvm.bean.RecommendPageBean;

import java.io.Serializable;

import static com.release.mvvm.utils.Constants.RECOMMEND_BUNDLE;
import static com.release.mvvm.utils.Constants.RECOMMEND_CTIME;
import static com.release.mvvm.utils.Constants.RECOMMEND_DESC;
import static com.release.mvvm.utils.Constants.RECOMMEND_HTML;
import static com.release.mvvm.utils.Constants.RECOMMEND_TITLE;

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


    public String setIntentData(Intent intent){


//        Bundle bundleExtra = intent.getBundleExtra(RECOMMEND_BUNDLE);
//        RecommendPageBean.NewslistBean bean = (RecommendPageBean.NewslistBean) bundleExtra.getSerializable(RECOMMEND_BUNDLE);
//        tvTitleContent.set(bean.getTitle());
//        tvSource.set(bean.getDescription());
//        tvTime.set(bean.getCtime());
//        return bean.getUrl();

        String mRecomTitle = intent.getStringExtra(RECOMMEND_TITLE);
        String mRecomTime = intent.getStringExtra(RECOMMEND_CTIME);
        String mRecomDesc = intent.getStringExtra(RECOMMEND_DESC);
        String mRecomHtml = intent.getStringExtra(RECOMMEND_HTML);

        tvTitleContent.set(mRecomTitle);
        tvSource.set(mRecomDesc);
        tvTime.set(mRecomTime);

        return mRecomHtml;
    }
}
