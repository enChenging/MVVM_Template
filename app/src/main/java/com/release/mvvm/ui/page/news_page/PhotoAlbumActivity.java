package com.release.mvvm.ui.page.news_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.release.base.base.BaseActivity;
import com.release.base.utils.StatusBarUtil;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.bean.PhotoSetInfoBean;
import com.release.mvvm.databinding.ActivityPhotoAlbumBinding;
import com.release.mvvm.ui.adapter.PhotoSetAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.release.mvvm.utils.Constants.PHOTO_SET_KEY;

/**
 * 图集
 *
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class PhotoAlbumActivity extends BaseActivity<ActivityPhotoAlbumBinding, PhotoAlbumViewModel> {


    @Inject
    PhotoSetAdapter mAdapter;

    private boolean mIsHideToolbar = false;
    private List<PhotoSetInfoBean.PhotosBean> mPhotos;
    private String mPhotoSetId;

    public static void start(Context context, String newsId) {
        Intent intent = new Intent(context, PhotoAlbumActivity.class);
        intent.putExtra(PHOTO_SET_KEY, newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_photo_album;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

        mPhotoSetId = getIntent().getStringExtra(PHOTO_SET_KEY);
        binding.toolBar.setToolBarBackgroundColor(R.color.transparent)
                .setBackDrawable(R.drawable.toolbar_back_white)
                .setTitleColor(R.color.white);
    }

    @Override
    public void initListener() {

        viewModel.finishLoadData.observe(this, new Observer<PhotoSetInfoBean>() {
            @Override
            public void onChanged(PhotoSetInfoBean photoSetInfoBean) {
                loadPhotoDataView(photoSetInfoBean);
            }
        });
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.loadData(this,mPhotoSetId);
    }

    public void loadPhotoDataView(PhotoSetInfoBean photoSetBean) {

        binding.toolBar.setTitleText(photoSetBean.getSetname());

        List<String> imgUrls = new ArrayList<>();
        mPhotos = photoSetBean.getPhotos();
        for (PhotoSetInfoBean.PhotosBean photo : mPhotos) {
            imgUrls.add(photo.getImgurl());
        }

        mAdapter.setData(imgUrls);
        binding.vpPhoto.setAdapter(mAdapter);
        binding.vpPhoto.setOffscreenPageLimit(imgUrls.size());

        binding.tvCount.setText(mPhotos.size() + "");
        binding.tvIndex.setText(1 + "/");
        binding.tvContent.setText(mPhotos.get(0).getNote());


        binding.vpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                binding.tvContent.setText(mPhotos.get(position).getNote());
                binding.tvIndex.setText((position + 1) + "/");
            }
        });
        mAdapter.setTapListener(new PhotoSetAdapter.OnTapListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    binding.dragLayout.scrollOutScreen(300);
                    binding.toolBar.animate().translationY(-binding.toolBar.getBottom()).setDuration(300);
                } else {
                    binding.dragLayout.scrollInScreen(300);
                    binding.toolBar.animate().translationY(0).setDuration(300);
                }
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.black));
    }
}
