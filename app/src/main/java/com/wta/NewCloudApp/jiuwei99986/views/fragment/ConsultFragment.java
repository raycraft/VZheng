package com.wta.NewCloudApp.jiuwei99986.views.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.adapter.ConsultAdapter;
import com.wta.NewCloudApp.jiuwei99986.base.BaseFragment;
import com.wta.NewCloudApp.jiuwei99986.model.ConsultData;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.model.SigData;
import com.wta.NewCloudApp.jiuwei99986.presenter.ConsultHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.RxToast;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.views.activity.LivePageActivity;
import com.wta.NewCloudApp.jiuwei99986.views.customs.EditDialog;
import com.wta.NewCloudApp.jiuwei99986.widget.DivItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by 小小程序员 on 2017/9/20.
 */

public class ConsultFragment extends BaseFragment {
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.go_top)
    ImageView goTop;
    private ConsultHelp consultHelp;
    private Map<String, String> map = new HashMap<>();
    private int currentPage = 1;
    private LinearLayoutManager linearLayoutManager;
    private ConsultAdapter myAdapter;
    private List<ConsultData.DataEntity> mlist = new ArrayList<>();
    ConsultData.DataEntity dataEntity;
    public static ConsultFragment getInstance() {
        ConsultFragment sf = new ConsultFragment();
        return sf;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.consult_fragment;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        consultHelp = new ConsultHelp();
        queryData();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new ConsultAdapter(R.layout.consult_item, mlist, getActivity());
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
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 dataEntity = (ConsultData.DataEntity) adapter.getItem(position);
                if (dataEntity.getLive_status().equals(Constants.ZERO)) {
                    RxToast.normal(getString(R.string.consult_no_start));
                    return;
                }
                toastDialog(dataEntity);

            }
        });
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ConsultData.DataEntity entity = (ConsultData.DataEntity) adapter.getItem(position);
                share(entity);
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
    private void toastDialog(final ConsultData.DataEntity dataEntity) {
        final EditDialog editDialog = new EditDialog(getActivity());
        editDialog.getEditText().setLines(1);
        editDialog.setTitle(getString(R.string.pass_word));
        editDialog.show();
        editDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                map.put("live_password", editDialog.getEditText().getText().toString());
                map.put(Constants.LIVE_ID,dataEntity.getLiveid());
                map.put(Constants.TYPE, "entry");
                consultHelp.ensureWord(map);
                editDialog.dismiss();
            }
        });
        editDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    private void share(final ConsultData.DataEntity dataEntity) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_share, null);
        ImageView weixin = view.findViewById(R.id.weixin);
        ImageView weiXinCircle = view.findViewById(R.id.weixin_circle);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.live_room_message, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWeixin(SendMessageToWX.Req.WXSceneSession,dataEntity);
                popupWindow.dismiss();
            }
        });
        weiXinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWeixin(SendMessageToWX.Req.WXSceneTimeline,dataEntity);
                popupWindow.dismiss();
            }
        });
    }

    private void shareWeixin(int scene,ConsultData.DataEntity dataEntity) {
        WXMediaMessage mediaMessage = new WXMediaMessage();
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = Constants.BASE_URL+"Consultant_share.php?liveid="+dataEntity.getLiveid();
        mediaMessage.description = dataEntity.getTitle();
        mediaMessage.mediaObject = webpageObject;
        mediaMessage.title = dataEntity.getNick();
        mediaMessage.setThumbImage(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = mediaMessage;
        req.transaction = "100";
        req.scene = scene;
        boolean b = App.wxapi.sendReq(req);
    }

    private void queryData() {
        map.clear();
        map.put(Constants.PAGE, currentPage + "");
        map.put(Constants.PAGE_SIZE, Constants.PAGE_NUMBER);
        consultHelp.getLiveList(map, currentPage);
    }

    @OnClick(R.id.go_top)
    public void onViewClick(View view) {
        //recyclerView.scrollToPosition(0);
        linearLayoutManager.scrollToPosition(0);
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
        consultHelp.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getSig(SigData data) {
        if (data==null){
            return;
        }
        if (data.getSign()==1){
            return;
        }
        if (data != null && data.getFlag().equals(Constants.ONE)) {
            if (TextUtils.isEmpty(SharedPrefrenceManager.getInstance().getSig())) {
                SharedPrefrenceManager.getInstance().setSig(data.getData().getSig());
                SharedPrefrenceManager.getInstance().setLiveNumber(data.getData().getPhonenumber());
            }

            Intent intent = new Intent(getActivity(), LivePageActivity.class);
            intent.putExtra(Constants.ROOM_LIVEID, dataEntity.getLiveid());
            intent.putExtra(Constants.OBJECT, dataEntity);
            intent.putExtra(Constants.SIGN_OBJECT, data);
            intent.putExtra(Constants.ROOM_TAG, Constants.JOIN_ROOM);
            getActivity().startActivity(intent);
        } else if (data != null && data.getFlag().equals(Constants.FLAG_FOUR)) {
            RxToast.normal(getString(R.string.error_word));
        } else {
            RxToast.normal(getString(R.string.view_network_error));
        }
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getLiveData(ConsultData data) {
        showContentView();
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
        }
        if (data != null) {
            mlist = data.getData();
            if (data.getSign() == 1) {
                if (mlist==null){
                    showEmptyView();
                    return;
                }
                myAdapter.getData().clear();
            }
            if (mlist == null) {
                smartRefreshLayout.setLoadmoreFinished(true);
            } else {
                myAdapter.addData(mlist);
            }
        }
    }


}
