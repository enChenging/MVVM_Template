package com.release.mvvm.ui.base;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.release.mvvm.App;
import com.release.mvvm.R;
import com.release.mvvm.utils.StatusBarUtil;
import com.release.mvvm.utils.SwipeRefreshHelper;
import com.release.mvvm.utils.baserx.Messenger;
import com.release.mvvm.widget.EmptyLayout;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends RxAppCompatActivity
        implements HasSupportFragmentInjector, UIIterfaceAct, EmptyLayout.OnRetryListener {

    protected static String TAG;
    protected T binding;
    protected V viewModel;
    private int viewModelId;

    SwipeRefreshLayout mSwipeRefresh;
    protected EmptyLayout mEmptyLayout;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TAG = this.getClass().getSimpleName();

        initViewDataBinding(savedInstanceState);

        registorUIChangeLiveDataCallBack();

        initView();

        initListener();

        initSwipeRefresh();

        updateViews(false);

        viewModel.registerRxBus();

        mEmptyLayout = findViewById(R.id.empty_layout);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));
    }


    private void initViewDataBinding(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutId(savedInstanceState));
        viewModelId = initVariableId();
        viewModel = initViewModel();

        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (V) createViewModel(App.getInstance(), modelClass);
        }

        binding.setVariable(viewModelId, viewModel);//关联viewModel

        getLifecycle().addObserver(viewModel);//LifecycleObserver（让viewModel拥有acitivity的生命周期）

        viewModel.injectLifecycleProvider(this);//注入RxLifecycle生命周期
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    protected void registorUIChangeLiveDataCallBack() {

        viewModel.getUC().getShowLoadingEvent().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showLoading();
            }
        });

        viewModel.getUC().getHideLoadingEvent().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                hideLoading();
            }
        });

        viewModel.getUC().getShowNetErrorEvent().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showNetError();
            }
        });

        viewModel.getUC().getFinishRefreshEvent().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                finishRefresh();
            }
        });

        viewModel.getUC().getOnRetryEvent().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                onRetry();
            }
        });

        //跳入新页面
        viewModel.getUC().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
                overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
            }
        });

        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                finish();
            }
        });
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                onBackPressed();
            }
        });
    }

    public V initViewModel() {
        return null;
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initView() {
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }

    private void initSwipeRefresh() {
        if (mSwipeRefresh != null) {
            SwipeRefreshHelper.init(mSwipeRefresh, new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateViews(true);
                }
            });
        }
    }


    public void showLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
        }
    }


    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }


    public void showNetError() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setRetryListener(this);
        }
    }

    @Override
    public void onRetry() {
        updateViews(false);
    }


    public void finishRefresh() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //ViewModel与ViewModel之间数据传递
        Messenger.getDefault().unregister(viewModel);

        getLifecycle().removeObserver(viewModel);

        if (viewModel != null) {
            viewModel.removeRxBus();
        }

        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Application application, Class<T> cls) {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(cls);
    }
}
