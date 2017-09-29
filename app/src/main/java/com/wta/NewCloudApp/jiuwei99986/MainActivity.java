package com.wta.NewCloudApp.jiuwei99986;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.custom.CommonTabLayout;
import com.wta.NewCloudApp.jiuwei99986.listener.CustomTabEntity;
import com.wta.NewCloudApp.jiuwei99986.model.TabEntity;
import com.wta.NewCloudApp.jiuwei99986.utils.PopupMenuUtil;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.utils.Utils;
import com.wta.NewCloudApp.jiuwei99986.views.activity.ActivityLogin;
import com.wta.NewCloudApp.jiuwei99986.views.fragment.MainFindFragment;
import com.wta.NewCloudApp.jiuwei99986.views.fragment.MyFragment;
import com.wta.NewCloudApp.jiuwei99986.views.fragment.ConsultFragment;
import com.wta.NewCloudApp.jiuwei99986.views.fragment.MainPageFragment;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {
    private FrameLayout frameLayout;
    private ImageView add;

    private CommonTabLayout commonTabLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"首页","发现","", "咨询室", "我的"};
    private int[] mIconUnselectIds = {R.drawable.mainpage,
            R.mipmap.find_gray, R.drawable.ic_chevron_right_gray,R.drawable.consult,
            R.mipmap.mine_gray};
    private int[] mIconSelectIds = {R.drawable.mainpage_blue,
            R.mipmap.find_blue, R.drawable.ic_chevron_right_gray,R.drawable.consult_blue,
            R.mipmap.mine};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private PopupMenuUtil popupMenuUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.test_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        //map.clear();
        if (getIntent().getStringExtra(Constants.FLAG)!=null){

        }

        popupMenuUtil =new PopupMenuUtil();
        add = (ImageView) findViewById(R.id.add);
        commonTabLayout = (CommonTabLayout) findViewById(R.id.common_tab);
        mFragments.add(MainPageFragment.getInstance());
        mFragments.add(MainFindFragment.getInstance());
        mFragments.add(MyFragment.getInstance(""));
        mFragments.add(ConsultFragment.getInstance());
        mFragments.add(MyFragment.getInstance(""));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commonTabLayout.setTabData(mTabEntities, this, R.id.fragment_contain, mFragments, true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPrefrenceManager.getInstance().getIsLogin()){
                    startActivity(new Intent(MainActivity.this,ActivityLogin.class));
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (SharedPrefrenceManager.getInstance().getIsLogin()){
                    popupMenuUtil._show(MainActivity.this, add);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (popupMenuUtil._isShowing()){
            popupMenuUtil._close(500);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popupMenuUtil.stopAnims();
        Utils.fixInputMethodManagerLeak(this);
    }
}
