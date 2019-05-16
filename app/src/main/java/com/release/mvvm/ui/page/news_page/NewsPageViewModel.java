package com.release.mvvm.ui.page.news_page;

import android.app.Application;

import androidx.annotation.NonNull;

import com.release.base.base.BaseViewModel;
import com.release.base.base.SingleLiveEvent;
import com.release.base.binding.command.BindingAction;
import com.release.base.binding.command.BindingCommand;
import com.release.base.utils.ToastUtils;
import com.release.mvvm.dao.DaoSession;
import com.release.mvvm.dao.NewsTypeInfo;
import com.release.mvvm.dao.NewsTypeInfoDao;

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
