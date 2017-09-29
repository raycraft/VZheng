package com.wta.NewCloudApp.jiuwei99986.views.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.adapter.FindAdapter;
import com.wta.NewCloudApp.jiuwei99986.base.BaseFragment;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.FindData;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.views.activity.FindContentActivity;
import com.wta.NewCloudApp.jiuwei99986.widget.DivItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


@SuppressLint("ValidFragment")
public class FindFragment extends BaseFragment {
    @BindView(R.id.go_top)
    ImageView goTop;
    private String mTitle;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<FindData.DataEntity> mlist = new ArrayList<>();
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    private FindAdapter myAdapter;
    private DataHelp dataHelp;
    private Map<String, String> map = new HashMap<>();
    private int currentPage = 1;

    public static FindFragment getInstance(String title) {
        FindFragment sf = new FindFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_fragment;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        showLoadingView();
        dataHelp = new DataHelp(getActivity());
        queryData();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new FindAdapter(R.layout.find_adpter_item, mlist, getActivity());
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                queryData();
                smartRefreshLayout.finishLoadmore();
                if (myAdapter.getItemCount() > 200) {
                    smartRefreshLayout.setLoadmoreFinished(true);
                    Toast.makeText(getActivity(), "已经到底了", Toast.LENGTH_SHORT).show();
                }

            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 1;
                queryData();

            }
        });
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FindData.DataEntity dataEntity = (FindData.DataEntity) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), FindContentActivity.class);
                intent.putExtra(Constants.INTENT_DATA, dataEntity.getTid());
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    goTop.setVisibility(View.INVISIBLE);
                } else {
                    goTop.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.go_top)
    public void onViewClick(View view) {
        //recyclerView.scrollToPosition(0);
        linearLayoutManager.scrollToPosition(0);
    }

    private void queryData() {
        map.clear();
        map.put(Constants.PAGE, "" + currentPage);
        map.put(Constants.PAGE_SIZE, "10");
        map.put(Constants.TYPE, "find");
        dataHelp.findData(map, currentPage);
    }

    @Override
    public void getErrorData(NoNetWork s) {
        super.getErrorData(s);
        showNoNetworkView();
    }

    @Override
    public void getError(ErrorData data) {
        super.getError(data);
        showErrorView();
    }

    @Override
    public void onNetworkViewRefresh() {
        super.onNetworkViewRefresh();
        queryData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataHelp.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getFindData(FindData findData) {
        showContentView();
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
        }
        if (findData != null) {
            mlist = findData.getData();
            if (findData.getTag() == 1) {
                myAdapter.getData().clear();
            }
            if (mlist == null) {
                smartRefreshLayout.setLoadmoreFinished(true);
            } else {
                myAdapter.addData(mlist);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPrefrenceManager.getInstance().getCanRefresh()) {
            currentPage = 1;
            queryData();
            SharedPrefrenceManager.getInstance().setCanRefresh(false);
        }

        //smartRefreshLayout.autoRefresh();
    }

}