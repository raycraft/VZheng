package com.wta.NewCloudApp.jiuwei99986;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by 小小程序员 on 2017/8/23.
 */

public class App extends Application{
    //static 代码段可以防止内存泄露
    //


    private RefWatcher refWatcher;
    public static IWXAPI wxapi;
    private static Context mContext;
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.light_gray, R.color.appColor);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        /*SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });*/
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        refWatcher=LeakCanary.install(this);
        ILiveSDK.getInstance().initSdk(this, Constants.SDK_APPID, Constants.ACCOUNT_TYPE);
        registerToWx();
        MultiDex.install(mContext);
    }

    private void registerToWx() {
        wxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID);
        wxapi.registerApp(Constants.WEIXIN_APPID);
    }

    public static Context getContext(){
        return mContext;
    }
    public static RefWatcher getRefWatcher(Context context) {
        App app= (App) context.getApplicationContext();
        return app.refWatcher;
    }
}
