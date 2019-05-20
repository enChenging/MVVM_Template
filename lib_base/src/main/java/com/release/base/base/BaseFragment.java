package com.release.base.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.release.base.R;
import com.release.base.dagger.qualifiers.ChildFragmentManager;
import com.release.base.utils.SwipeRefreshHelper;
import com.release.base.utils.baserx.Messenger;
import com.release.base.widget.EmptyLayout;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment implements HasSupportFragmentInjector,
        UIIterfaceFrag, EmptyLayout.OnRetryListener {

    protected T binding;
    protected V viewModel;
    private int viewModelId;
    public BaseActivity mActivity;

    EmptyLayout mEmptyLayout;
    SwipeRefreshLayout mSwipeRefresh;

    @Inject
    protected Context mContext;
    @Inject
    @ChildFragmentManager
    protected FragmentManager childFragmentManager;
    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }


    @Override
    public void onAttach(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here for versions before M as onAttach(*Context*) did not yet exist
            // This fixes DaggerFragment issue: https://github.com/google/dagger/issues/777
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(activity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(*Activity*).
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(inflater, container, savedInstanceState), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmptyLayout = view.findViewById(R.id.empty_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);

        initViewDataBinding();
        registorUIChangeLiveDataCallBack();
        initView(view);
        initListener();
        initSwipeRefresh();
        updateViews(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Messenger.getDefault().unregister(viewModel);
        getLifecycle().removeObserver(viewModel);

        if (binding != null)
            binding.unbind();
    }

    private void initViewDataBinding() {
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
            viewModel = (V) createViewModel(BaseApplication.getInstance(), modelClass);
        }
        binding.setVariable(viewModelId, viewModel);
        getLifecycle().addObserver(viewModel);
    }


    public V initViewModel() {
        return null;
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initView(View view) {
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
            }
        });

        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                getActivity().finish();
            }
        });
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragmentq.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragmentq.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
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

    public void finishRefresh() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onRetry() {
        updateViews(false);
    }


    public boolean isBackPressed() {
        return false;
    }


    /**
     * 初始化下拉刷新
     */
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

    public <T extends ViewModel> T createViewModel(Application application, Class<T> cls) {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(cls);
    }
}
