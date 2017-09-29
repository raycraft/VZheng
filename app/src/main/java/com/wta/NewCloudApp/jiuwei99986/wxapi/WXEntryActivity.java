package com.wta.NewCloudApp.jiuwei99986.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.model.WeiXinData;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.RxToast;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by 小小程序员 on 2017/9/19.
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private DataHelp dataHelp;
    private Map<String,String> map=new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        App.wxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == baseResp.getType()) ;
                else RxToast.normal(getResources().getString(R.string.toast_wexin_login_fail));

                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token

                        final String code = ((SendAuth.Resp) baseResp).code;
                        dataHelp=new DataHelp(this);
                        map.put("code",code);
                        map.put("appID",Constants.WEIXIN_APPID);
                        map.put("secret",Constants.WEIXIN_SECRET);
                        map.put("platform","1");
                        dataHelp.wexinCode(map);
                       // Log.i("TAG", "onResp: map.size "+map.size());
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        finish();
                        break;
                }
                break;
        }

    }
    @Subscribe(threadMode = ThreadMode.PostThread)
    public void getWeiXinData(WeiXinData data){
        if (data!=null){
            SharedPrefrenceManager.getInstance().setNick(data.getNick());
            SharedPrefrenceManager.getInstance().setUserId(data.getUserid());
            SharedPrefrenceManager.getInstance().setIsLogin(true);
            Intent intent=new Intent();
            intent.setAction(Constants.BROADCAST);
            sendBroadcast(intent);
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (dataHelp!=null){
            dataHelp.onDestroy();
        }
    }
}
