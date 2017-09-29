package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVText;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.adapter.ChatMsgListAdapter;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.listener.LiveView;
import com.wta.NewCloudApp.jiuwei99986.model.ChatEntity;
import com.wta.NewCloudApp.jiuwei99986.model.ConsultData;
import com.wta.NewCloudApp.jiuwei99986.model.MemberInfo;
import com.wta.NewCloudApp.jiuwei99986.model.MySelf;
import com.wta.NewCloudApp.jiuwei99986.model.SigData;
import com.wta.NewCloudApp.jiuwei99986.presenter.ChatPresenter;
import com.wta.NewCloudApp.jiuwei99986.presenter.ChatView;
import com.wta.NewCloudApp.jiuwei99986.presenter.LiveHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.RxToast;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.views.customs.MembersDialog;
import com.wta.NewCloudApp.jiuwei99986.widget.GlideCircleTransform;
import com.wta.NewCloudApp.jiuwei99986.widget.InputTextMsgDialog;
import com.wta.NewCloudApp.jiuwei99986.widget.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by 小小程序员 on 2016/12/27.
 */

public class LivePageActivity extends BaseActivity implements View.OnClickListener, ChatView, LiveView {
    private String ROOM_TAG = "", ROOM_LIVEID, DOCTOR_ICON = "", DOCTOR_NAME = "", HOST_ID;
    private ChatPresenter presenter;
    private ImageView doctor_head, share;
    private RelativeLayout relativeLayout;
    ConsultData.DataEntity dataEntity;
    private boolean isHost = false, isSaveChat = false;
    private List<ChatEntity> chatEntityList = new ArrayList<>();
    private ArrayList<ChatEntity> mArrayListChatEntity;
    private String out_trade_no;
    private SharedPrefrenceManager sharedPrefrenceManager;
    private ListView listView;
    //private TimerTask mTimerTask = null;
    private List<String> smallphotoList = new ArrayList<>();
    private ILiveRoomManager manager;
    private float beautyNumble = 0;
    private Camera mcamera;
    private boolean mBoolRefreshLock = false;
    private boolean mBoolNeedRefresh = false;
    private Dialog mDetailDialog;
    private LinearLayout watcher, anchor, mVideoMemberCtrlView;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = 1;
    private TextView doctor_name, mDetailTime, hide, mDetailWatchCount, watchNumble, show;
    private Dialog mMemberDg, inviteDg;
    private int watchNumbles = 1;
    private static final int REFRESH_LISTVIEW = 5;
    private final Timer mTimer = new Timer();
    private static final int MINFRESHINTERVAL = 500;
    private static final int LEAVEROOM = 0x3;
    private static final int ENTER_LIVEROOM = 0x10;
    private static final int GET_HEAD = 0x9;
    private static final int HOST_LEAVEROOM = 0x7;
    private static final int APPLY_CODE = 51;
    private static final int HOST_CREATEROOM = 0x6;
    private TextView interact, BtnCtrlVideo, BtnCtrlMic, BtnHungup, imChat, BtnBeauty, camera, close, anchorClose;
    private RecyclerView listImage;
    private List<Integer> gifts = new ArrayList<>();
    private String userId, nick, nickName, nickPhoto;
    private RecyclerView.Adapter photoAdapter;
    private String sendName, sendText;
    private ILVLiveConfig config;
    private LiveHelp liveHelper;
    private AVRootView mRootView;
    private boolean isCanSendMessage = false;
    private ChatMsgListAdapter chatListAdapter;
    private Intent intent;
    private Map<String, String> dataMap = new HashMap<>();
    private Handler mhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDAT_WALL_TIME_TIMER_TASK:
                    break;
                case TIMEOUT_INVITE:
                    String id = "" + msg.obj;
                    cancelInviteView(id);
                    liveHelper.sendGroupCmd(Constants.AVIMCMD_MULTI_HOST_CANCELINVITE, id);
                    break;
                case REFRESH_LISTVIEW:
                    doRefreshListView();
                    break;
                case HOST_CREATEROOM:
                    //hostAndWatcherJoinRoom("entry", Constant.HOST);
                    break;
                case LEAVEROOM:
                    //hostAndWatcherJoinRoom("leave", "");
                    break;
                case HOST_LEAVEROOM:
                    //hostAndWatcherJoinRoom("leave", Constant.HOST);
                    break;
                case APPLY_CODE:
                    if (msg.arg1 == 1) {
                        isApply = true;
                        interact.setBackgroundResource(R.drawable.interact);
                    } else {
                        isApply = false;
                        Toast.makeText(LivePageActivity.this, "申请失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ENTER_LIVEROOM:
                    MySelf self = (MySelf) msg.obj;
                    if (self.getData() != null) {
                        nickPhoto = self.getData().getPhoto();
                        nick = self.getData().getNick();
                        if (nick != null) {
                            //sharedPrefrenceManager.setRealName(nick);
                        }
                        if (self.getData().getOnline_number() != null) {
                            watchNumbles = Integer.parseInt(self.getData().getOnline_number()) + 1;
                            watchNumble.setText(watchNumbles + "");
                        }
                        if (self.getData().getSign().equals(Constants.HOST)) {
                            watchNumble.setText(watchNumbles + "");
                            doctor_name.setText(nick != null ? nick : "主播医生");
                            Glide.with(LivePageActivity.this).load(nickPhoto).error(R.mipmap.ic_launcher).into(doctor_head);
                        } else {
                        }
                        if (self.getData().getSmallphotos() != null) {
                            for (int i = 0; i < self.getData().getSmallphotos().size(); i++) {
                                smallphotoList.add(self.getData().getSmallphotos().get(i));
                            }
                            photoAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case GET_HEAD:
                    watchNumbles++;
                    watchNumble.setText(watchNumbles + "");
                    smallphotoList.add(0, (String) msg.obj);
                    photoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private String joinLiveId;
    private String backGroundId;
    private String formatTime;

    protected void init() {
        sharedPrefrenceManager = sharedPrefrenceManager.getInstance();
        userId = sharedPrefrenceManager.getUserId();
        intent = getIntent();
        ROOM_LIVEID = intent.getStringExtra(Constants.ROOM_LIVEID);
        ROOM_TAG = intent.getStringExtra(Constants.ROOM_TAG);
        interact = (TextView) findViewById(R.id.video_interact);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        doctor_head = (ImageView) findViewById(R.id.head_icon);
        share = (ImageView) findViewById(R.id.share);
        close = (TextView) findViewById(R.id.live_page_close);
        hide = (TextView) findViewById(R.id.live_page_anchor_hide);
        show = (TextView) findViewById(R.id.normal_btn);
        anchorClose = (TextView) findViewById(R.id.live_page_anchor_close);
        BtnBeauty = (TextView) findViewById(R.id.live_page_beauty);
        watchNumble = (TextView) findViewById(R.id.live_watch_numble);
        camera = (TextView) findViewById(R.id.live_page_camera);
        listView = (ListView) findViewById(R.id.live_page_im_list);
        listImage = (RecyclerView) findViewById(R.id.recycle_head);
        relativeLayout = (RelativeLayout) findViewById(R.id.live_content_contain);
        BtnCtrlVideo = (TextView) findViewById(R.id.camera_controll);
        BtnCtrlMic = (TextView) findViewById(R.id.mic_controll);
        BtnHungup = (TextView) findViewById(R.id.close_member_video);

        imChat = (TextView) findViewById(R.id.live_page_im);
        anchor = (LinearLayout) findViewById(R.id.live_anchor);
        watcher = (LinearLayout) findViewById(R.id.live_watch);
        mVideoMemberCtrlView = (LinearLayout) findViewById(R.id.video_member_bottom_layout);
        presenter = new ChatPresenter(config, this, SharedPrefrenceManager.getInstance().getUserId(), TIMConversationType.Group);
        listImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        photoAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.head_image, smallphotoList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                RoundImageView imageView = helper.getView(R.id.head_image);
                Glide.with(LivePageActivity.this).load(item).into(imageView);
            }
        };
        listImage.setAdapter(photoAdapter);
        setOnViewsClickListener(BtnCtrlVideo, BtnCtrlMic, BtnHungup, interact, camera, hide, BtnBeauty, close, imChat, anchorClose, share);
        //TODO 获取渲染层
        mRootView = (AVRootView) findViewById(R.id.av_root_view);
        //TODO 设置渲染层
        manager = ILiveRoomManager.getInstance();
        manager.initAvRootView(mRootView);
        config = new ILVLiveConfig();
        manager.init(config);

        config.messageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                List<TIMMessage> tlist = list;
                parseMessage(tlist);
                return false;
            }
        });
        config.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {
            @Override
            public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
            }

            @Override
            public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {

            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {

            }
        });
        mRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.h50));
        mRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.h1));
        mRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.h10));
        mRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.h70));
        mRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.h90));
        //manager.getIMGroupId()
        mRootView.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
            @Override
            public void onSubViewCreated() {
                for (int i = 0; i < 4; i++) {
                    mRootView.getViewByIndex(i).setRotate(true);

                    final int index = i;
                    AVVideoView avVideoView = mRootView.getViewByIndex(index);
                    avVideoView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            mRootView.swapVideoView(0, index);
                            backGroundId = mRootView.getViewByIndex(0).getIdentifier();
                            backGroundId = mRootView.getViewByIndex(0).getIdentifier();
//                            updateHostLeaveLayout();
                            if (isHost) {//自己是主播
                                if (backGroundId.equals(SharedPrefrenceManager.getInstance().getLiveNumber())) {//背景是自己
                                    anchor.setVisibility(View.VISIBLE);
                                    mVideoMemberCtrlView.setVisibility(View.GONE);
                                } else {//背景是其他成员
                                    anchor.setVisibility(View.GONE);
                                    mVideoMemberCtrlView.setVisibility(View.VISIBLE);
                                }
                            } else {//自己成员方式
                                if (backGroundId.equals(SharedPrefrenceManager.getInstance().getLiveNumber())) {//背景是自己
                                    mVideoMemberCtrlView.setVisibility(View.VISIBLE);
                                    watcher.setVisibility(View.GONE);
                                } else {//主播自己
                                    mVideoMemberCtrlView.setVisibility(View.GONE);
                                    watcher.setVisibility(View.VISIBLE);
                                }
                            }

                            return super.onSingleTapConfirmed(e);
                        }
                    });
                   /* mRootView.getViewByIndex(i).setDiffDirectionRenderMode(AVVideoView.ILiveRenderMode.SCALE_TO_FIT);
                    mRootView.getViewByIndex(i).setSameDirectionRenderMode(AVVideoView.ILiveRenderMode.SCALE_TO_FIT);*/
                }

            }

        });
        mArrayListChatEntity = new ArrayList<ChatEntity>();
        chatListAdapter = new ChatMsgListAdapter(this, listView, mArrayListChatEntity);
        listView.setAdapter(chatListAdapter);
        presenter.start();
    }

    /**
     * 主播邀请应答框
     */
    private void initInviteDialog() {
        inviteDg = new Dialog(this, R.style.dialog);
        inviteDg.setContentView(R.layout.invite_dialog);
        final TextView hostId = (TextView) inviteDg.findViewById(R.id.host_id);
        hostId.setText("");
        TextView agreeBtn = (TextView) inviteDg.findViewById(R.id.invite_agree);
        TextView refusebtn = (TextView) inviteDg.findViewById(R.id.invite_refuse);
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mVideoMemberCtrlView.setVisibility(View.VISIBLE);
//                mNomalMemberCtrView.setVisibility(View.INVISIBLE);
                //上麦 ；TODO 上麦 上麦 上麦 ！！！！！；
                liveHelper.sendC2CCmd(Constants.AVIMCMD_MUlTI_JOIN, "", HOST_ID);
                liveHelper.upMemberVideo();
                inviteDg.dismiss();
            }
        });

        refusebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveHelper.sendC2CCmd(Constants.AVIMCMD_MUlTI_REFUSE, "", HOST_ID);
                inviteDg.dismiss();
            }
        });

        Window dialogWindow = inviteDg.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void notifyRefreshListView(ChatEntity entity) {
        mBoolNeedRefresh = true;
        chatEntityList.add(entity);
        doRefreshListView();

    }

    private void doRefreshListView() {
        if (mBoolNeedRefresh) {
            mBoolRefreshLock = true;
            mBoolNeedRefresh = false;
            mArrayListChatEntity.addAll(chatEntityList);
            chatEntityList.clear();
            chatListAdapter.notifyDataSetChanged();
            /*if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mhandle.sendEmptyMessage(REFRESH_LISTVIEW);
                }
            };
            //mTimer.cancel();
            mTimer.schedule(mTimerTask, MINFRESHINTERVAL);*/
        } else {
            mBoolRefreshLock = false;
        }
    }


    protected void loadData() {
        liveHelper = new LiveHelp();
        liveHelper.setChatView(this);
        //结束直播间页面
        initBackDialog();
        //退出直播间页面
        initDetailDailog();
        //录制视频页面
        //initRecordDialog();
        //进入直播间
        if (ROOM_TAG.equals(Constants.JOIN_ROOM)) {
            isHost = false;
            dataEntity = (ConsultData.DataEntity) getIntent().getSerializableExtra(Constants.OBJECT);
            SigData data = (SigData) getIntent().getSerializableExtra(Constants.SIGN_OBJECT);
            initLiveData(data);
            liveHelper.loginTenCent(1, ROOM_LIVEID, self.getData().getPhonenumber(), self.getData().getSig());
            watcher.setVisibility(View.VISIBLE);
            anchor.setVisibility(View.GONE);
            watchNumbles = Integer.parseInt(data.getData().getAnchorid()) + 1;
            isSaveChat = data.getChat().equals(Constants.ONE) ? true : false;
            //interact.setVisibility(View.GONE);
            //liveHelper.joinRoom(ROOM_LIVEID);
            initInviteDialog();
            share.setVisibility(View.GONE);
        } else {
            //创建直播间
            isHost = true;
            mhandle.sendEmptyMessage(HOST_CREATEROOM);
            mMemberDg = new MembersDialog(this, R.style.floag_dialog, this, ROOM_LIVEID);
            inviteView1 = (TextView) findViewById(R.id.invite_view1);
            inviteView2 = (TextView) findViewById(R.id.invite_view2);
            inviteView3 = (TextView) findViewById(R.id.invite_view3);
            inviteView1.setOnClickListener(this);
            inviteView2.setOnClickListener(this);
            inviteView3.setOnClickListener(this);
            loginTenCent();

            //liveHelper.createRoom(ROOM_LIVEID);
        }

    }

    //直播间头像昵称等初始化
    private void initLiveData(SigData data) {
        self = data;
        nick = data.getData().getNick();
        nickPhoto = data.getData().getPhoto();
        doctor_name.setText(data.getData().getCounselor_nick() != null ? data.getData().getCounselor_nick() : "主播医生");
        Glide.with(this).load(data.getData().getCounselor_photo()).error(R.mipmap.ic_launcher).transform(new GlideCircleTransform(this)).into(doctor_head);
        HOST_ID = data.getData().getAnchorid();
    }

    private void loginTenCent() {
        if (isHost) {
            initLoginData();
            dataMap.put(Constants.USERID, SharedPrefrenceManager.getInstance().getUserId());
            liveHelper.enterExitLive(dataMap);
        } /*else {
            dataMap.put("live_password", "");
        }*/
    }

    public void initLoginData() {
        dataMap.clear();
        dataMap.put(Constants.TYPE, "entry");
        dataMap.put(Constants.LIVE_ID, ROOM_LIVEID);
    }

    SigData self;
    Thread thread;
    boolean canRun = true;


    private void setOnViewsClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    private void initDetailDailog() {
        mDetailDialog = new Dialog(this, R.style.dialog);
        mDetailDialog.setContentView(R.layout.dialog_live_detail);
        mDetailTime = (TextView) mDetailDialog.findViewById(R.id.tv_time);
        //mDetailAdmires = (TextView) mDetailDialog.findViewById(R.id.tv_admires);
        mDetailWatchCount = (TextView) mDetailDialog.findViewById(R.id.tv_members);
        mDetailDialog.setCancelable(false);
        TextView tvCancel = (TextView) mDetailDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mhandle.sendEmptyMessage(HOST_LEAVEROOM);
                mDetailDialog.dismiss();
                //SharedPrefrenceManager.getInstance(LivePageActivity.this).setLiveAudit(false);
                finish();
            }
        });
//        mDetailDialog.show();
    }


    private void inputMsgDialog() {
        InputTextMsgDialog inputMsgDialog = new InputTextMsgDialog(this, this, R.style.inputdialog);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = inputMsgDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        inputMsgDialog.getWindow().setAttributes(lp);
        inputMsgDialog.setCancelable(true);
        inputMsgDialog.show();
    }

    private Dialog backDialog;

    private void initBackDialog() {
        backDialog = new Dialog(this, R.style.dialog);
        backDialog.setContentView(R.layout.dialog_end_live);
        TextView tvSure = (TextView) backDialog.findViewById(R.id.btn_sure);
        TextView title = (TextView) backDialog.findViewById(R.id.dialog_title);
        if (ROOM_TAG.equals(Constants.JOIN_ROOM)) {
            title.setText("退出直播间");
        }
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isHost) {
                   /* mhandle.sendEmptyMessage(LEAVEROOM);
                    if (nickName == null) {
                        nickName = "匿名";
                    }
                    if (nickName.trim().length() < 1) {
                        nickName = "匿名";
                    }*/
                    presenter.sendMessage(Constants.WATCHER_LEAVE + nick + ":离开房间了" + ":" + "", "-1", manager, LivePageActivity.this);
                    manager.quitRoom(new ILiveCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            dataMap.clear();
                            dataMap.put(Constants.TYPE, "leave");
                            dataMap.put(Constants.LIVE_ID, ROOM_LIVEID);
                            liveHelper.enterExitLive(dataMap);
                            backDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {

                        }
                    });

                    return;
                }
                //如果是主播离开，发消息
                presenter.sendMessage(Constants.HOST_LEAVE, "-1", manager, LivePageActivity.this);
                //liveHelper.stopRecord();
                manager.quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                        dataMap.clear();
                        dataMap.put(Constants.USERID, SharedPrefrenceManager.getInstance().getUserId());
                        dataMap.put(Constants.TYPE, "leave");
                        dataMap.put(Constants.LIVE_ID, ROOM_LIVEID);
                        liveHelper.enterExitLive(dataMap);
                        mDetailTime.setText(formatTime);
                        mDetailDialog.show();
                        mDetailWatchCount.setText(watchNumbles + "");
                        //modifyLiveState("live_end");
                        backDialog.dismiss();
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });
            }
        });
        TextView tvCancel = (TextView) backDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDialog.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        quiteLiveByPurpose();
        clearOldData();
    }

    private void quiteLiveByPurpose() {
        if (backDialog != null) {
            if (backDialog.isShowing() == false) {
                backDialog.show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_page_anchor_hide:
            case R.id.live_page_hide:
                relativeLayout.setVisibility(View.GONE);
                show.setVisibility(View.VISIBLE);
                break;
            case R.id.normal_btn:
                relativeLayout.setVisibility(View.VISIBLE);
                show.setVisibility(View.GONE);
                break;
            case R.id.close_member_video:
                cancelMemberView(backGroundId);
                break;
            case R.id.camera_controll:
                if (SharedPrefrenceManager.getInstance().getLiveNumber().equals(backGroundId)) {//自己关闭自己
                    switchCamera();
                } else {
                    liveHelper.sendC2CCmd(Constants.AVIMCMD_MULTI_HOST_CONTROLL_CAMERA, backGroundId, backGroundId);
                }
                break;
            case R.id.mic_controll:
                if (HOST_ID == null) {
                    HOST_ID = SharedPrefrenceManager.getInstance().getLiveNumber();
                }
                if (HOST_ID.equals(SharedPrefrenceManager.getInstance().getLiveNumber())) {
                    toggleMic();
                } else {
                    liveHelper.sendC2CCmd(Constants.AVIMCMD_MULTI_HOST_CONTROLL_MIC, backGroundId, backGroundId);//主播关闭自己
                }
                break;
            case R.id.share:
                share();
               /* UMImage umimage = null;
                String shareContent = "";
                String shareTitle = "";
                String url = Constant.SERVICE_BASE + "liveshare.php?liveid=" + ROOM_LIVEID;
                if (ROOM_TAG.equals(Constant.JOIN_ROOM)) {
                    umimage = new UMImage(this, getIntent().getStringExtra(Constant.DOCTOR_ICON));
                    umimage.setThumb(new UMImage(LivePageActivity.this, getIntent().getStringExtra(Constant.LIVE_IMAGE)));
                    shareContent = getIntent().getStringExtra(Constant.LIVE_TITLE);
                    shareTitle = DOCTOR_NAME + "在[唯美结缘]做直播,快来围观吧！";
                } else {
                    umimage = new UMImage(this, nickPhoto);
                    umimage.setThumb(new UMImage(this, nickPhoto));
                    shareContent = SharedPrefrenceManager.getInstance(this).getTitle();
                    shareTitle = sharedPrefrenceManager.getNick() + "在[唯美结缘]做直播,快来围观吧！";
                }
                new ShareAction(LivePageActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withTitle(shareTitle)
                        .withMedia(umimage)
                        .withTargetUrl(url)
                        .withText(shareContent)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                presenter.sendMessage("分享了直播", ROOM_LIVEID, manager, LivePageActivity.this);
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .open();*/
                break;

            case R.id.live_page_close:
            case R.id.live_page_anchor_close:
                // chatView.sendText();
                // sendText("再不出来我打你了啊");
                quiteLiveByPurpose();
                // liveHelper.startExitRoom();
                break;

            case R.id.live_page_camera:
                switchCamera();
                break;
            case R.id.live_page_beauty:
                if (beautyNumble == 0) {
                    ILiveSDK.getInstance().getAvVideoCtrl().inputWhiteningParam(4.5f);
                    ILiveSDK.getInstance().getAvVideoCtrl().inputBeautyParam(4.5f);
                    beautyNumble = 1.5f;
                } else {
                    ILiveSDK.getInstance().getAvVideoCtrl().inputWhiteningParam(0);
                    ILiveSDK.getInstance().getAvVideoCtrl().inputBeautyParam(0);
                    beautyNumble = 0;
                }
                break;
            case R.id.live_page_im:
                inputMsgDialog();
                break;
            case R.id.invite_view1:
                inviteView1.setVisibility(View.INVISIBLE);
                liveHelper.sendGroupCmd(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, "" + inviteView1.getTag());
                break;
            case R.id.invite_view2:
                inviteView2.setVisibility(View.INVISIBLE);
                liveHelper.sendGroupCmd(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, "" + inviteView1.getTag());
                break;
            case R.id.invite_view3:
                inviteView3.setVisibility(View.INVISIBLE);
                liveHelper.sendGroupCmd(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, "" + inviteView1.getTag());
                break;
            case R.id.video_interact:
                if (isHost) {
                    mMemberDg.setCanceledOnTouchOutside(true);
                    mMemberDg.show();
                } else {
                    if (!isApply) {
                        dataMap.clear();
                        dataMap.put(Constants.LIVE_ID, ROOM_LIVEID);
                        dataMap.put("memberphone", SharedPrefrenceManager.getInstance().getLiveNumber());
                        liveHelper.audienceApply(dataMap);
                    }
                }
                break;
        }
    }

    private void share() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_share, null);
        ImageView weixin = view.findViewById(R.id.weixin);
        ImageView weiXinCircle = view.findViewById(R.id.weixin_circle);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.live_room_message, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWeixin(SendMessageToWX.Req.WXSceneSession);
            }
        });
        weiXinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWeixin(SendMessageToWX.Req.WXSceneTimeline);
            }
        });
    }

    private void shareWeixin(int scene) {
        WXImageObject wxImageObject = new WXImageObject();
        wxImageObject.setImagePath("http://img.weimeijieyuan.com/uploads/wzyl/2017/20170925/20170925171313625.jpg");
        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.description = "我在V美整形在做直播,快来围观吧";
        mediaMessage.mediaObject = wxImageObject;
        mediaMessage.title = self.getData().getNick();
        mediaMessage.messageExt = "hahah";
        mediaMessage.setThumbImage(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = mediaMessage;
        req.transaction = "100";
        req.scene = scene;
        boolean b = App.wxapi.sendReq(req);
    }

    private boolean isApply;

    private void parseMessage(List<TIMMessage> tlist) {
        for (int i = tlist.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = tlist.get(i);
            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null)
                    continue;
                TIMElem elem = currMsg.getElement(j);
                TIMUserProfile profile = currMsg.getSenderProfile();
                String identifier = profile.getIdentifier();
                //上麦消息
                if (elem.getType() == TIMElemType.Custom) {
                    String customText = null;
                    try {
                        customText = new String(((TIMCustomElem) elem).getData(), "UTF-8");
                        JSONTokener jsonParser = new JSONTokener(customText);
                        // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
                        // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
                        JSONObject json = (JSONObject) jsonParser.nextValue();
                        int action = json.getInt(Constants.CMD_KEY);
                        switch (action) {
                            case Constants.AVIMCMD_MUlTI_HOST_INVITE:
                                showInviteDialog();
                                break;
                            case Constants.AVIMCMD_MUlTI_JOIN:
                                cancelInviteView(identifier);
                                break;
                            case Constants.AVIMCMD_MUlTI_REFUSE:
                                cancelInviteView(identifier);
                                break;
                            case Constants.AVIMCMD_MULTI_CANCEL_INTERACT://主播关闭摄像头命令
                                //如果是自己关闭Camera和Mic
                                String closeId = json.getString(Constants.CMD_PARAM);
                                if (closeId.equals(SharedPrefrenceManager.getInstance().getLiveNumber())) {//是自己
                                    //TODO 被动下麦 下麦 下麦
                                    liveHelper.downMemberVideo();
                                }
                                //其他人关闭小窗口
                                ILiveRoomManager.getInstance().getRoomView().closeUserView(closeId, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                                hideInviteDialog();
                                refreshUI(closeId);
                                break;
                            case Constants.AVIMCMD_MULTI_HOST_CANCELINVITE:
                                hideInviteDialog();
                                break;
                            case Constants.AVIMCMD_MULTI_HOST_CONTROLL_CAMERA:
//                    toggleCamera();
                                switchCamera();
                                break;
                            case Constants.AVIMCMD_MULTI_HOST_CONTROLL_MIC:
                                toggleMic();
                                break;

                            case Constants.AVIMCMD_HOST_BACK:

                            default:
                                break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //最后处理文本消息
                if (elem.getType() == TIMElemType.Text) {
                    TIMTextElem textElem = (TIMTextElem) elem;
                    ChatEntity entity = new ChatEntity();
                    int sign = 0;
                    entity.setType(0);
                    String text = textElem.getText();
                    //判断主播是否离开房间
                    if (text.equals(Constants.HOST_LEAVE)) {
                        mhandle.sendEmptyMessage(LEAVEROOM);
                        manager.quitRoom(new ILiveCallBack() {
                            @Override
                            public void onSuccess(Object data) {
                            }

                            @Override
                            public void onError(String module, int errCode, String errMsg) {
                            }
                        });
                        Toast.makeText(LivePageActivity.this, "主播已经离开房间", Toast.LENGTH_SHORT).show();
                        if (backDialog != null) {
                            if (backDialog.isShowing()) {
                                backDialog.dismiss();
                            }
                        }
                        finish();
                    } else {
                        if (text.substring(0, 2).equals("$|")) {
                        } else if (text.substring(0, 2).equals("&|")) {
                            String content = text.substring(2, text.length());
                            String[] split = content.split(":");
                            //&|小郑医生：进入房间了:http://201712.png
                            if (split != null) {
                                if (split.length > 1) {
                                    sendName = split[0];
                                    if (sendName.equals(nickName)) {
                                        break;
                                    }
                                    sendText = split[1];
                                    sign = 2;
                                    Message message = mhandle.obtainMessage();
                                    message.what = GET_HEAD;
                                    String headUrl = "";
                                    if (split.length > 3) {
                                        headUrl = "http:" + split[3];
                                    }
                                    message.obj = headUrl;
                                    mhandle.sendMessage(message);
                                }
                            }

                        } else if (text.length() > 4 && text.substring(0, 4).equals(Constants.WATCHER_LEAVE)) {
                            String content = text.substring(4, text.length());
                            String[] split = content.split(":");
                            watchNumbles--;
                            if (watchNumbles < 1) {
                                watchNumbles = 1;
                            }
                            watchNumble.setText(watchNumbles + "");
                            if (split != null) {
                                if (split.length > 3) {
                                    String headUrl = "http:" + split[3];
                                    if (smallphotoList.contains(headUrl)) {
                                        smallphotoList.remove(headUrl);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            break;
                        } else {
                            sendName = text.substring(0, text.indexOf(":")) + ":";
                            sendText = text.substring(text.indexOf(":") + 1);
                        }
                        if (sendName.trim().length() < 1) {
                            sendName = "匿名";
                        }
                        entity.setSenderName(sendName);
                        entity.setSign(sign);
                        entity.setContext(sendText);
                        listView.setVisibility(View.VISIBLE);
                        notifyRefreshListView(entity);
                        if (listView.getCount() > 1) {
                            if (true)
                                listView.setSelection(0);
                            else
                                listView.setSelection(listView.getCount() - 1);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void showMessage(TIMMessage message) {

    }

    public void showInviteDialog() {
        if ((inviteDg != null) && (getBaseContext() != null) && (inviteDg.isShowing() != true)) {
            inviteDg.show();
        }
    }

    // 清除老房间数据
    private void clearOldData() {
        mArrayListChatEntity.clear();
        mBoolNeedRefresh = true;
        if (mBoolRefreshLock) {
            return;
        } else {
            doRefreshListView();
        }
        //mRootView.clearUserView();
    }

    @Override
    public void showMessage(List<TIMMessage> messages) {
    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

    }

    @Override
    public void sendText(String msg) {
        if (msg.equals(Constants.JOIN_ERROR)) {
            RxToast.normal(getString(R.string.consult_close));
            finish();
        }
        if (isSaveChat){
            dataMap.clear();
            dataMap.put(Constants.LIVE_ID,ROOM_LIVEID);
            dataMap.put("content",nick+":"+msg);
            liveHelper.chat_record(dataMap);
        }

        if (msg.equals(Constants.JOIN_ROOM)) {
            if (nickPhoto == null) {
                nickPhoto = "";
            }
            presenter.sendMessage("&|" + nick + ":进入房间了" + ":" + nickPhoto, "-1", manager, LivePageActivity.this);
            return;
        }

        if (msg.length() == 0 || msg.trim().length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(LivePageActivity.this, "input message too long", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        ChatEntity chat = new ChatEntity();
        chat.setContext(msg);
        chat.setType(0);
        //chat.setSign(1);
        if (nick == null) {
            nick = "匿名";
        }
        chat.setSenderName(nick);
        listView.setVisibility(View.VISIBLE);
        notifyRefreshListView(chat);
        if (listView.getCount() > 1) {
            if (true)
                listView.setSelection(0);
            else
                listView.setSelection(listView.getCount() - 1);
        }
        if (ROOM_TAG.equals(Constants.JOIN_ROOM)) {
            presenter.sendMessage(msg, nick, manager, this);
        }
    }

    @Override
    public void showDraft(TIMMessageDraft draft) {

    }

    @Override
    public void startRecordCallback(boolean isSucc) {

    }

    @Override
    public void stopRecordCallback(boolean isSucc, List<String> files) {

    }

    protected boolean onFocus(MotionEvent motionEvent, Camera.AutoFocusCallback callback) {
        if (ROOM_TAG.equals(Constants.JOIN_ROOM)) {
            return false;
        }
        if (ILiveRoomManager.getInstance().getCurCameraId() == ILiveConstants.FRONT_CAMERA) {
            return false;
        }
        mcamera = (Camera) ILiveSDK.getInstance().getAvVideoCtrl().getCamera();
        if (mcamera == null) {
            return false;
        }
        Camera.Parameters parameters = null;
        try {
            parameters = mcamera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //不支持设置自定义聚焦，则使用自动聚焦，返回
        if (Build.VERSION.SDK_INT >= 14) {
            if (parameters.getMaxNumFocusAreas() <= 0) {
                return focus(mcamera, callback);
            }
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            int left = (int) (motionEvent.getX() - 300);
            int top = (int) (motionEvent.getY() - 300);
            int right = (int) (motionEvent.getX() + 300);
            int bottom = (int) (motionEvent.getY() + 300);
            left = left < -1000 ? -1000 : left;
            top = top < -1000 ? -1000 : top;
            right = right > 1000 ? 1000 : right;
            bottom = bottom > 1000 ? 1000 : bottom;
            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 1000));
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setFocusAreas(areas);
            try {
                //兼容部分定制机型，在此捕捉异常，对实际聚焦效果没影响
                mcamera.setParameters(parameters);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }

        return focus(mcamera, callback);
    }

    private boolean focus(Camera camera, Camera.AutoFocusCallback callback) {
        try {
            camera.autoFocus(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.live_page_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
        loadData();
    }

    private int inviteViewCount = 0;
    private static final int TIMEOUT_INVITE = 20;
    private TextView inviteView1, inviteView2, inviteView3;

    @Override
    public boolean showInviteView(String id) {
        int index = mRootView.findValidViewIndex();
        if (index == -1) {
            return false;
        }
        int requetCount = index + inviteViewCount;
        if (requetCount > 3) {
            return false;
        }

        if (hasInvited(id)) {
            return false;
        }
        switch (requetCount) {
            case 1:
                inviteView1.setText("正在连接...");
                inviteView1.setVisibility(View.VISIBLE);
                inviteView1.setTag(id);

                break;
            case 2:
                inviteView2.setText("正在连接...");
                inviteView2.setVisibility(View.VISIBLE);
                inviteView2.setTag(id);
                break;
            case 3:
                inviteView3.setText("正在连接...");
                inviteView3.setVisibility(View.VISIBLE);
                inviteView3.setTag(id);
                break;
        }
        liveHelper.sendC2CCmd(Constants.AVIMCMD_MUlTI_HOST_INVITE, "", id);
        inviteViewCount++;
        //30s超时取消
        Message msg = new Message();
        msg.what = TIMEOUT_INVITE;
        msg.obj = id;
        mhandle.sendMessageDelayed(msg, 30 * 1000);
        return true;
    }

    private boolean bMicOn;

    public void toggleMic() {
        bMicOn = !bMicOn;
        ILiveRoomManager.getInstance().enableMic(bMicOn);
    }

    /**
     * 判断是否邀请过同一个人
     */
    private boolean hasInvited(String id) {
        if (id.equals(inviteView1.getTag())) {
            return true;
        }
        if (id.equals(inviteView2.getTag())) {
            return true;
        }
        if (id.equals(inviteView3.getTag())) {
            return true;
        }
        return false;
    }

    @Override
    public void cancelInviteView(String id) {
        if ((inviteView1 != null) && (inviteView1.getTag() != null)) {
            if (inviteView1.getTag().equals(id)) {
            }
            if (inviteView1.getVisibility() == View.VISIBLE) {
                inviteView1.setVisibility(View.INVISIBLE);
                inviteView1.setTag("");
                inviteViewCount--;
            }
        }

        if (inviteView2 != null && inviteView2.getTag() != null) {

            if (inviteView2.getTag().equals(id)) {
                if (inviteView2.getVisibility() == View.VISIBLE) {
                    inviteView2.setVisibility(View.INVISIBLE);
                    inviteView2.setTag("");
                    inviteViewCount--;
                }
            } else {
            }
        } else {
        }

        if (inviteView3 != null && inviteView3.getTag() != null) {
            if (inviteView3.getTag().equals(id)) {
                if (inviteView3.getVisibility() == View.VISIBLE) {
                    inviteView3.setVisibility(View.INVISIBLE);
                    inviteView3.setTag("");
                    inviteViewCount--;
                }
            } else {
            }
        } else {
        }


    }

    public void hideInviteDialog() {
        if ((inviteDg != null) && (inviteDg.isShowing() == true)) {
            inviteDg.dismiss();
        }
    }

    public void refreshUI(String id) {
        //当主播选中这个人，而他主动退出时需要恢复到正常状态
        if (isHost) {
            if (backGroundId == null) {
                backGroundId = SharedPrefrenceManager.getInstance().getLiveNumber();
            }
            if (!backGroundId.equals(SharedPrefrenceManager.getInstance().getLiveNumber()) && backGroundId.equals(id)) {
                backToNormalCtrlView();
            }
        }

    }

    private void backToNormalCtrlView() {

        if (isHost) {
            backGroundId = SharedPrefrenceManager.getInstance().getLiveNumber();
            anchor.setVisibility(View.VISIBLE);
            mVideoMemberCtrlView.setVisibility(View.GONE);
        } else {
            backGroundId = HOST_ID;
            watcher.setVisibility(View.VISIBLE);
            mVideoMemberCtrlView.setVisibility(View.GONE);
        }
    }

    public void switchCamera() {
        switch (ILiveRoomManager.getInstance().getCurCameraId()) {
            case ILiveConstants.FRONT_CAMERA:
                ILiveRoomManager.getInstance().switchCamera(ILiveConstants.BACK_CAMERA);
                break;
            case ILiveConstants.BACK_CAMERA:
                ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
                break;
        }

    }

    @Override
    public void cancelMemberView(String id) {
        if (!isHost) {
            liveHelper.downMemberVideo();
        }
        liveHelper.sendGroupCmd(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, id);
        mRootView.closeUserView(id, AVView.VIDEO_SRC_TYPE_CAMERA, true);
        backToNormalCtrlView();
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void getMicState(MemberInfo data) {
        if (data != null && data.getFlag().equals(Constants.ONE)) {
            isApply = true;
            interact.setBackgroundResource(R.drawable.interact);
        } else {
            isApply = false;
            RxToast.normal(getString(R.string.mic_fail));
        }
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void getSig(SigData data) {
        if (data != null && data.getFlag().equals(Constants.ONE) && data.getSign() == 1) {
            isSaveChat = data.getChat().equals(Constants.ONE) ? true : false;
            if (TextUtils.isEmpty(SharedPrefrenceManager.getInstance().getSig())) {
                SharedPrefrenceManager.getInstance().setSig(data.getData().getSig());
                SharedPrefrenceManager.getInstance().setLiveNumber(data.getData().getPhonenumber());
            }
            initLiveData(data);
            if (isHost) {
                HOST_ID = data.getData().getPhonenumber();
                liveHelper.loginTenCent(0, ROOM_LIVEID, self.getData().getPhonenumber(), self.getData().getSig());
            }
        } else if (data != null && data.getFlag().equals(Constants.FLAG_FOUR)) {
            RxToast.normal(getString(R.string.error_word));

        } else {
            RxToast.normal(getString(R.string.view_network_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILiveRoomManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ILiveRoomManager.getInstance().onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        liveHelper.onDestroy();
        ILiveRoomManager.getInstance().onDestory();
        /*isQuit = true;
        clearOldData();
        //presenter.
        canRun = false;
        finish();*/
    }
}
