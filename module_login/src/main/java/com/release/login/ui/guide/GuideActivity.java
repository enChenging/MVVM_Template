package com.release.login.ui.guide;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.release.base.base.BaseActivity;
import com.release.base.utils.StatusBarUtil;
import com.release.base.widget.pageTransformer.CubeOutTransformer;
import com.release.login.BR;
import com.release.login.R;
import com.release.login.databinding.ActivityGuideBinding;
import com.release.login.ui.adapter.GuideViewPagerAdapter;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class GuideActivity extends BaseActivity<ActivityGuideBinding, GuideViewModel> {


    private int dot_width;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_guide;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initListener() {

        binding.viewViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int translate = (int) (dot_width * (position + positionOffset));
                binding.dotFocus.setTranslationX(translate);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == viewModel.imageList.size() - 1) {
                    binding.btHome.setVisibility(View.VISIBLE);
                } else {
                    binding.btHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.dotFocus.postDelayed(() -> dot_width = binding.dotGroup.getChildAt(1).getLeft() - binding.dotGroup.getChildAt(0).getLeft(), 5);
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.imageViews(this, binding.dotGroup);
        binding.viewViewpager.setAdapter(new GuideViewPagerAdapter(viewModel.imageList));
        //效果
//        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
//        viewpager.setPageTransformer(true, new DepthPageTransformer());
//        viewpager.setPageTransformer(true, new RotatePageTransformer());
        binding.viewViewpager.setPageTransformer(true, new CubeOutTransformer());
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
