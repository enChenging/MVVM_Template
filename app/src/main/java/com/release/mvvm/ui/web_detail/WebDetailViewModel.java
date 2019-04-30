package com.release.mvvm.ui.web_detail;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.release.mvvm.ui.base.BaseViewModel;

import static com.release.mvvm.ui.base.Constants.RECOMMEND_CTIME;
import static com.release.mvvm.ui.base.Constants.RECOMMEND_DESC;
import static com.release.mvvm.ui.base.Constants.RECOMMEND_HTML;
import static com.release.mvvm.ui.base.Constants.RECOMMEND_TITLE;

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
