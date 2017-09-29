package com.wta.NewCloudApp.jiuwei99986.views.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.base.BaseFragment;
import com.wta.NewCloudApp.jiuwei99986.model.MyMessage;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.views.activity.ActivityLogin;
import com.wta.NewCloudApp.jiuwei99986.views.activity.ConsultHomeActivity;
import com.wta.NewCloudApp.jiuwei99986.views.activity.ModifyMessageActivity;
import com.wta.NewCloudApp.jiuwei99986.views.activity.MyAskActivity;
import com.wta.NewCloudApp.jiuwei99986.views.activity.MySayActivity;
import com.wta.NewCloudApp.jiuwei99986.widget.RoundImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@SuppressLint("ValidFragment")
public class MyFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_abl_app_bar)
    AppBarLayout mAblBar;
    @BindView(R.id.main_tv_toolbar_title)
    TextView title;
    @BindView(R.id.my_say)
    LinearLayout mySay;
    @BindView(R.id.my_ask)
    LinearLayout myAsk;
    @BindView(R.id.head)
    RoundImageView head;

    @BindView(R.id.my_setting)
    LinearLayout mySetting;
    @BindView(R.id.my_nick)
    TextView myNick;
    @BindView(R.id.say_number)
    TextView sayNumber;
    @BindView(R.id.ask_number)
    TextView askNumber;
    @BindView(R.id.main_fl_title)
    RelativeLayout mainFlTitle;
    private DataHelp dataHelp;
    private Map<String, RequestBody> postMap = new HashMap<>();
    private MyMessage myMessage;
    private MyBroadCast broadCast;
    private String nick;
    public static MyFragment getInstance(String title) {
        MyFragment sf = new MyFragment();
        return sf;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_fragment;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        toolbar.setTitle("");
        dataHelp = new DataHelp(getContext());
        queryData();
        initListener();
        IntentFilter intentfilter=new IntentFilter();
        intentfilter.addAction(Constants.BROADCAST);
        broadCast=new MyBroadCast();
        getContext().registerReceiver(broadCast,intentfilter);
    }

    private void queryData() {
        postMap.clear();
        postMap.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
        postMap.put("type", RequestBody.create(MediaType.parse("text/plain"), "basic"));
        dataHelp.modifyMessage(postMap);
    }

    protected void initListener() {
        mAblBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int halfScroll = appBarLayout.getTotalScrollRange() / 2;
                int offSetAbs = Math.abs(verticalOffset);
                float percentage;
                if (offSetAbs < halfScroll) {
                    title.setText(getString(R.string.my_title));
                    percentage = 1 - (float) offSetAbs / (float) halfScroll;
                } else {
                    title.setText(nick);
                    percentage = (float) (offSetAbs - halfScroll) / (float) halfScroll;
                }
                toolbar.setAlpha(percentage);
            }
        });
    }

    @OnClick({R.id.my_say, R.id.my_ask, R.id.my_setting,R.id.my_reLogin,R.id.my_consult})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.my_ask:
                startActivity(new Intent(getActivity(), MyAskActivity.class));
                break;
            case R.id.my_say:
                startActivity(new Intent(getActivity(), MySayActivity.class));
                break;
            case R.id.my_setting:
                Intent intent =new Intent(getActivity(), ModifyMessageActivity.class);
                intent.putExtra(Constants.OBJECT,myMessage);
                startActivity(intent);
                break;
            case R.id.my_reLogin:
                SharedPrefrenceManager.getInstance().setIsLogin(false);
                SharedPrefrenceManager.getInstance().clear();
                startActivity(new Intent(getActivity(), ActivityLogin.class));
                break;
            case R.id.my_consult:
                startActivity(new Intent(getActivity(), ConsultHomeActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void followResult(MyMessage data) {
        if (data != null) {
            myMessage=data;
            Glide.with(getActivity()).load(data.getData().getPhoto()).error(R.mipmap.ic_launcher).into(head);
            askNumber.setText(data.getData().getAsknums());
            sayNumber.setText(data.getData().getFindnums());
            myNick.setText(data.getData().getNick());
            nick=data.getData().getNick();
        }
    }

    class MyBroadCast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            queryData();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataHelp.onDestroy();
        getContext().unregisterReceiver(broadCast);

    }


}