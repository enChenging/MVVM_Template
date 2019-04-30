package com.release.mvvm.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.databinding.ActivityMainBinding;
import com.release.mvvm.ui.base.BaseActivity;
import com.release.mvvm.utils.StatusBarUtil;
import com.release.mvvm.utils.ToastUtils;

import cn.jzvd.Jzvd;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mHeadImg;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initView() {
        View headerView = binding.leftNavigation.getHeaderView(0);
        binding.leftNavigation.setItemIconTintList(null);
        mHeadImg = headerView.findViewById(R.id.headImg);

        binding.bottomNavigation.enableAnimation(false);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_nav);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    @Override
    public void initListener() {

        binding.dlDrawer.setScrimColor(getResources().getColor(R.color.black_alpha_32));
        binding.dlDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView != null && drawerView.getTag().equals("left")) {
                    View content = binding.dlDrawer.getChildAt(0);
                    int offset = (int) (drawerView.getWidth() * slideOffset);
                    content.setTranslationX(offset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        binding.leftNavigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public void updateViews(boolean isRefresh) {
        Glide.with(this).load("https://b-ssl.duitang.com/uploads/item/201802/20/20180220170028_JcYMU.jpeg").circleCrop().into(mHeadImg);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_help_center:
                ToastUtils.show("帮助中心");
                break;
            case R.id.nav_setting:
                ToastUtils.show("设置");
                break;
            case R.id.nav_camera:
                ToastUtils.show("照相机");
                break;
            case R.id.nav_gallery:
                ToastUtils.show("相册");
                break;
        }
        toggle();
        return false;
    }


    public void toggle() {
        viewModel.toggle(binding.dlDrawer);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewModel.exit(closeDrawableLayout());
            return false;
        }


        return super.onKeyDown(keyCode, event);
    }

    public boolean closeDrawableLayout() {
        if (binding.dlDrawer.isDrawerVisible(GravityCompat.START)) {
            binding.dlDrawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, binding.dlDrawer, getResources().getColor(R.color.colorPrimary), 0);
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }

}
