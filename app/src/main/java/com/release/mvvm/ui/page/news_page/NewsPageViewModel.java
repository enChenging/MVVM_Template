package com.release.mvvm.ui.page.news_page;

import android.app.Application;

import androidx.annotation.NonNull;

import com.release.mvvm.binding.command.BindingAction;
import com.release.mvvm.binding.command.BindingCommand;
import com.release.mvvm.dao.DaoSession;
import com.release.mvvm.dao.NewsTypeInfo;
import com.release.mvvm.dao.NewsTypeInfoDao;
import com.release.mvvm.ui.base.BaseViewModel;
import com.release.mvvm.ui.base.SingleLiveEvent;
import com.release.mvvm.utils.ToastUtils;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/4/30
 * @Describe
 */
public class NewsPageViewModel extends BaseViewModel {
    private static final String TAG = NewsPageViewModel.class.getSimpleName();

    public SingleLiveEvent setting = new SingleLiveEvent();
    public SingleLiveEvent<List<NewsTypeInfo>> finishLoadData = new SingleLiveEvent();

    public NewsPageViewModel(@NonNull Application application) {
        super(application);
    }


    public BindingCommand settingClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            setting.call();
        }
    });

    public BindingCommand searchClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.show("搜索");
        }
    });


    public void loadData(DaoSession daoSession) {

        NewsTypeInfoDao newsTypeInfoDao = daoSession.getNewsTypeInfoDao();
        List<NewsTypeInfo> newsTypeInfos = newsTypeInfoDao.loadAll();
        finishLoadData.setValue(newsTypeInfos);
    }

}
