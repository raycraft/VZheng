package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.adapter.AskAdapter;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.FindData;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 小小程序员 on 2017/9/12.
 */

public class MyAskActivity extends BaseActivity{
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    private List<FindData.DataEntity> mlist = new ArrayList<>();
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;

    private AskAdapter myAdapter;
    private DataHelp dataHelp;
    private Map<String,RequestBody> map =new HashMap<>();
    private int currentPage = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.universal_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        showLoadingView();
        dataHelp =new DataHelp(this);
        queryData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new AskAdapter(R.layout.find_adpter_item, mlist,this);
        recyclerView.setAdapter(myAdapter);
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                queryData();
                smartRefreshLayout.finishLoadmore();
                if (myAdapter.getItemCount()>100){
                    smartRefreshLayout.setLoadmoreFinished(true);
                    Toast.makeText(MyAskActivity.this, "已经到底了", Toast.LENGTH_SHORT).show();
                }

            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage=1;
                queryData();

            }
        });
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FindData.DataEntity dataEntity = (FindData.DataEntity) adapter.getData().get(position);
                Intent intent =new Intent(MyAskActivity.this,FindContentActivity.class);
                intent.putExtra(Constants.INTENT_DATA,dataEntity.getTid());
                intent.putExtra(Constants.FLAG,dataEntity.getFid());
                startActivity(intent);
            }
        });
    }
    private void queryData() {
        map.clear();
       /* map.put(Constants.PAGE,""+currentPage);
        map.put(Constants.PAGE_SIZE,"10");
        map.put(Constants.TYPE,"ask");
        map.put(Constants.USERID,Constants.USERID);*/

        map.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
        map.put(Constants.TYPE, RequestBody.create(MediaType.parse("text/plain"), "ask"));
        map.put(Constants.PAGE_SIZE, RequestBody.create(MediaType.parse("text/plain"), Constants.PAGE_NUMBER));
        map.put(Constants.PAGE, RequestBody.create(MediaType.parse("text/plain"), "" + currentPage));
        dataHelp.myAskData(map,currentPage);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getFindData(FindData findData){
        showContentView();
        if (smartRefreshLayout.isRefreshing()){
            smartRefreshLayout.finishRefresh();
        }
        if (findData!=null){
            if (findData.getSign()==1){
                myAdapter.getData().clear();
                if (findData.getData()==null){
                    showEmptyView();
                    return;
                }
            }
            mlist = findData.getData();
            myAdapter.addData(mlist);
        }else {
            showEmptyView();
        }
    }

    @Override
    public void getErrorData(NoNetWork data) {
        super.getErrorData(data);
        showNoNetworkView();
    }

    @Override
    public void getError(ErrorData data) {
        super.getError(data);
        showErrorView();
    }

    @Override
    public void onResume() {
        super.onResume();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataHelp.onDestroy();

    }
}
