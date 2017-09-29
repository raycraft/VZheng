package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.LiveRoomMessage;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.presenter.ConsultHelp;
import com.wta.NewCloudApp.jiuwei99986.widget.DivItemDecoration;
import com.wta.NewCloudApp.jiuwei99986.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by 小小程序员 on 2017/9/20.
 */

public class ConsultHomeActivity extends BaseActivity {
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    private ConsultHelp consultHelp;
    private LinearLayoutManager linearLayoutManager;
    private BaseQuickAdapter adapter;
    private List<LiveRoomMessage.DataEntity> mlist = new ArrayList<>();
    @BindView(R.id.tool_content)
    TextView title;
    @Override
    protected int getLayoutId() {
        return R.layout.universal_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        title.setText(getString(R.string.consult_my));
        consultHelp = new ConsultHelp();
        queryData();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BaseQuickAdapter<LiveRoomMessage.DataEntity, BaseViewHolder>(R.layout.consult_home_item, mlist) {
            @Override
            protected void convert(BaseViewHolder helper, final LiveRoomMessage.DataEntity item) {
                helper.setText(R.id.consult_home_number, "房间" + item.getLiveid())
                        .setText(R.id.consult_home_content, item.getLive_description());
                RoundImageView constrainImage = helper.getView(R.id.consult_home_image);
                Glide.with(ConsultHomeActivity.this).load(item.getLive_cover()).into(constrainImage);
                helper.getView(R.id.consult_start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ConsultHomeActivity.this, LivePageActivity.class);
                        intent.putExtra(Constants.ROOM_LIVEID,item.getLiveid());
                        intent.putExtra(Constants.ROOM_TAG,Constants.CREATE_ROOM);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DivItemDecoration(this, false));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ConsultHomeActivity.this, ModifyLiveMessage.class);
                LiveRoomMessage.DataEntity dataEntity=(LiveRoomMessage.DataEntity) adapter.getItem(position);
                intent.putExtra(Constants.FLAG, dataEntity);
                startActivity(intent);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
           queryData();
            }
        });
    }

    private void queryData() {
        consultHelp.getMyLive();
    }

    @Override
    public void getErrorData(NoNetWork s) {
        super.getErrorData(s);
        showNoNetworkView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
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
    protected void onDestroy() {
        super.onDestroy();
        consultHelp.onDestroy();

    }

    @OnClick(R.id.toolbar_back)
    public void onViewsClick(View view) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getLiveData(LiveRoomMessage data) {
        showContentView();
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
        }
        if (data != null) {
            adapter.getData().clear();
            mlist = data.getData();
            adapter.addData(mlist);
        }
    }
}
