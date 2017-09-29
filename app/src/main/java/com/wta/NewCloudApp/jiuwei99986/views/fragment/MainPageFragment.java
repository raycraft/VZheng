package com.wta.NewCloudApp.jiuwei99986.views.fragment;

import android.os.Bundle;

import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.base.BaseFragment;

/**
 * Created by 小小程序员 on 2017/9/19.
 */

public class MainPageFragment extends BaseFragment {
    public static MainPageFragment getInstance() {
        MainPageFragment sf = new MainPageFragment();
        return sf;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_page_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

    }
}
