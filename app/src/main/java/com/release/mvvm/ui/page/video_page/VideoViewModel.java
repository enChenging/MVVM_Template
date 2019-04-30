package com.release.mvvm.ui.page.video_page;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.release.mvvm.ui.base.BaseViewModel;

import java.util.ArrayList;

/**
 * @author Mr.release
 * @create 2019/4/29
 * @Describe
 */
public class VideoViewModel extends BaseViewModel {

    private final String[] VIDEO_ID = new String[]{
            "V9LG4B3A0", "V9LG4E6VR", "V9LG4CHOR", "00850FRB"
    };

    public final String[] VIDEO_TITLE = new String[]{
            "热点", "搞笑", "娱乐", "精品"
    };
    public ArrayList<Fragment> mFragments;


    public VideoViewModel(@NonNull Application application) {
        super(application);
    }

    public void initData() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < VIDEO_ID.length; i++) {
            mFragments.add(VideoListFragment.newInstance(VIDEO_ID[i]));
        }
    }
}
