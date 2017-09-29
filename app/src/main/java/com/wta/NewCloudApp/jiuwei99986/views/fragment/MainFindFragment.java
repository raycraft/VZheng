package com.wta.NewCloudApp.jiuwei99986.views.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.model.TabEntity;
import com.wta.NewCloudApp.jiuwei99986.custom.CommonTabLayout;
import com.wta.NewCloudApp.jiuwei99986.listener.CustomTabEntity;
import com.wta.NewCloudApp.jiuwei99986.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MainFindFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private List<String> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.mipmap.find_gray, R.mipmap.add_launcher,
            R.mipmap.mine_gray};
    private int[] mIconSelectIds = {
            R.mipmap.find_blue, R.mipmap.add_launcher,
            R.mipmap.mine};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"发现", "科普", "问医生"};
    private ViewPager viewPager;
    private CommonTabLayout commonTabLayout;
    public static MainFindFragment getInstance() {
        MainFindFragment sf = new MainFindFragment();
        return sf;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_main_card, null);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        commonTabLayout = (CommonTabLayout) v.findViewById(R.id.head_common_tab);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(FindFragment.getInstance(""));
        mFragments.add(ScienceFragment.getInstance(""));
        mFragments.add(AskFragment.getInstance(""));
        viewPager.setOffscreenPageLimit(2);
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);
        return v;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        commonTabLayout.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}