package com.release.mvvm.ui.page.video_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.release.base.base.BaseFragment;
import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.dao.VideoInfo;
import com.release.mvvm.databinding.FragmentVideoListBinding;
import com.release.mvvm.ui.adapter.VideoListAdapter;

import java.util.List;

import javax.inject.Inject;

import cn.jzvd.Jzvd;

import static com.release.mvvm.utils.Constants.VIDEO_ID_KEY;

/**
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class VideoListFragment extends BaseFragment<FragmentVideoListBinding, VideoListViewModel> {

    private static final String TAG = VideoListFragment.class.getSimpleName();
    @Inject
    VideoListAdapter mAdapter;
    public String mVideoId;

    public static VideoListFragment newInstance(String videoId) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID_KEY, videoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_video_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView(View view) {

        mVideoId = getArguments().getString(VIDEO_ID_KEY);
        binding.rvPhotoList.setHasFixedSize(true);
        binding.rvPhotoList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPhotoList.setAdapter(mAdapter);
    }


    @Override
    public void initListener() {
        binding.rvPhotoList.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                        Jzvd.resetAllVideos();
                    }
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                viewModel.loadMoreData(mVideoId);
            }
        }, binding.rvPhotoList);


        viewModel.finishLoadData.observe(this, new Observer<List<VideoInfo>>() {
            @Override
            public void onChanged(List<VideoInfo> videoInfos) {
                mAdapter.setNewData(videoInfos);
            }
        });

        viewModel.finishLoadMoreData.observe(this, new Observer<List<VideoInfo>>() {
            @Override
            public void onChanged(List<VideoInfo> videoInfos) {
                mAdapter.loadMoreComplete();
                mAdapter.addData(videoInfos);
            }
        });

        viewModel.finishNoData.observe(this, new Observer<List<VideoInfo>>() {
            @Override
            public void onChanged(List<VideoInfo> videoInfos) {
                mAdapter.loadMoreEnd();
            }
        });
    }

    @Override
    public void updateViews(boolean isRefresh) {
        viewModel.loadData(this,isRefresh, mVideoId);
    }

}
