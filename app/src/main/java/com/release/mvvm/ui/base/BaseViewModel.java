package com.release.mvvm.ui.base;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.trello.rxlifecycle3.LifecycleProvider;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by goldze on 2017/6/15.
 */
public class BaseViewModel extends BaseModel  implements IBaseViewModel {
    private UIChangeLiveData uc;
    private WeakReference<LifecycleProvider> lifecycle;

    public BaseViewModel(@NonNull Application application) {
        super(application);

    }

    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    public void showLoading() {
        uc.showLoadingEvent.call();
    }


    public void hideLoading() {
        uc.hideLoadingEvent.call();
    }


    public void showNetError() {
        uc.showNetErrorEvent.call();
    }


    public void onRetry() {
        uc.onRetryEvent.call();
    }


    public void finishRefresh() {
        uc.finishRefreshEvent.call();
    }

    /**
     * 关闭界面
     */
    public void finish() {
        uc.finishEvent.call();
    }

    /**
     * 返回上一层
     */
    public void onBackPressed() {
        uc.onBackPressedEvent.call();
    }


    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startActivityEvent.postValue(params);
    }


    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerRxBus() {
    }

    @Override
    public void removeRxBus() {
    }

    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<Void> showLoadingEvent;
        private SingleLiveEvent<Void> hideLoadingEvent;
        private SingleLiveEvent<Void> showNetErrorEvent;
        private SingleLiveEvent<Void> onRetryEvent;
        private SingleLiveEvent<Void> finishRefreshEvent;
        private SingleLiveEvent<Void> finishEvent;
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Void> onBackPressedEvent;

        public SingleLiveEvent<Void> getShowLoadingEvent() {
            return showLoadingEvent = createLiveData(showLoadingEvent);
        }

        public SingleLiveEvent<Void> getHideLoadingEvent() {
            return hideLoadingEvent = createLiveData(hideLoadingEvent);
        }

        public SingleLiveEvent<Void> getShowNetErrorEvent() {
            return showNetErrorEvent = createLiveData(showNetErrorEvent);
        }

        public SingleLiveEvent<Void> getOnRetryEvent() {
            return onRetryEvent = createLiveData(onRetryEvent);
        }

        public SingleLiveEvent<Void> getFinishRefreshEvent() {
            return finishRefreshEvent = createLiveData(finishRefreshEvent);
        }

        public SingleLiveEvent<Void> getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }


        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        private SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
