package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.model.LoginData;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.RegexpUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.RxToast;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class ActivityLogin extends BaseActivity {


    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView mIvCleanPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.clean_password)
    ImageView mCleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView mIvShowPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.forget_password)
    TextView mForgetPassword;
    @BindView(R.id.get_code)
    TextView getCode;
    private TimeCount time;
    @BindView(R.id.root)
    LinearLayout mRoot;
    private DataHelp dataHelp;
    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private float scale = 0.6f; //logo缩放比例
    private int height = 0;
    private Map<String, RequestBody> map = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_act;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initEvent();
    }

    private void initView() {
        mBtnLogin.setEnabled(false);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
        dataHelp = new DataHelp(this);
        time = new TimeCount(60000, 1000);
    }

    private void initEvent() {
        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(mEtPassword.getText().toString())) {
                    mBtnLogin.setEnabled(true);
                } else {
                    mBtnLogin.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanPhone.getVisibility() == View.GONE) {
                    mIvCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPhone.setVisibility(View.GONE);
                }
            }
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(mEtMobile.getText().toString())) {
                    mBtnLogin.setEnabled(true);
                } else {
                    mBtnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPassword.getVisibility() == View.GONE) {
                    mCleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    RxToast.normal(getResources().getString(R.string.toast_number));
                    s.delete(temp.length() - 1, temp.length());
                    mEtPassword.setSelection(s.length());
                }
            }
        });
    }

    public boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    @OnClick({R.id.iv_clean_phone, R.id.weixin_login, R.id.clean_password, R.id.iv_show_pwd, R.id.get_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                mEtMobile.setText("");
                break;
            case R.id.clean_password:
                mEtPassword.setText("");
                break;
            case R.id.iv_show_pwd:
                if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvShowPwd.setImageResource(R.drawable.pass_visuable);
                } else {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvShowPwd.setImageResource(R.drawable.pass_gone);
                }
                String pwd = mEtPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    mEtPassword.setSelection(pwd.length());
                break;
            case R.id.get_code:
                if (!RegexpUtils.isMobileNO(mEtMobile.getText().toString())) {
                    Toast.makeText(ActivityLogin.this, getResources().getString(R.string.toast_phone), Toast.LENGTH_SHORT).show();
                    return;
                }
                time.start();
                map.clear();
                map.put("phonenumber", RequestBody.create(MediaType.parse("text/plain"), mEtMobile.getText().toString()));
                map.put("type", RequestBody.create(MediaType.parse("text/plain"), "code"));

                dataHelp.loginApi(map);
                break;
            case R.id.btn_login:
                if (!RegexpUtils.isMobileNO(mEtMobile.getText().toString())) {
                    Toast.makeText(ActivityLogin.this, getResources().getString(R.string.toast_phone), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mEtMobile.getText().toString())) {
                    Toast.makeText(ActivityLogin.this, getResources().getString(R.string.toast_code), Toast.LENGTH_SHORT).show();
                    return;
                }
                map.clear();
                /*map.put("phonenumber",  mEtMobile.getText().toString());
                map.put("code",  mEtPassword.getText().toString());
                map.put("type",  "login");*/
                map.put("phonenumber", RequestBody.create(MediaType.parse("text/plain"), mEtMobile.getText().toString()));
                map.put("code", RequestBody.create(MediaType.parse("text/plain"), mEtPassword.getText().toString()));
                map.put("type", RequestBody.create(MediaType.parse("text/plain"), "login"));

                dataHelp.loginApi(map);
                break;
            case R.id.weixin_login:
                weiXinLogin();
                break;
        }
    }

    private void weiXinLogin() {
        if (!App.wxapi.isWXAppInstalled()) {
            Toast.makeText(ActivityLogin.this, getResources().getString(R.string.wexin_content), Toast.LENGTH_SHORT).show();
        return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        App.wxapi.sendReq(req);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //getCode.setBackgroundColor(Color.parseColor("#b9b9b9"));
            if (getCode != null) {
                getCode.setClickable(false);
                getCode.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
                getCode.setTextColor(getResources().getColor(R.color.gray));
                getCode.setTextSize(12);
            }
        }

        @Override
        public void onFinish() {
            if (getCode != null) {
                getCode.setText("获取验证码");
                getCode.setClickable(true);
                getCode.setTextColor(getResources().getColor(R.color.appColor));
                getCode.setTextSize(16);
            }
            //getCode.setBackgroundColor(Color.parseColor("#ffffff"));

        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void getLoginCode(LoginData data) {
        if (data.getData() != null && data.getData().getFlag().equals(Constants.ONE)) {
            SharedPrefrenceManager.getInstance().setIsLogin(true);
            SharedPrefrenceManager.getInstance().setUserId(data.getData().getUserid());
            SharedPrefrenceManager.getInstance().setNick(data.getData().getNick());
            Intent intent=new Intent();
            intent.setAction(Constants.BROADCAST);
            sendBroadcast(intent);
            finish();
        }
        if (data.getData().getFlag().equals(Constants.FLAG_SIX)) {
            Toast.makeText(ActivityLogin.this, getResources().getString(R.string.toast_time_out), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefrenceManager.getInstance().getIsLogin()){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHelp.onDestroy();
        time.cancel();
        time.onFinish();
        time = null;
    }
}
