package com.wta.NewCloudApp.jiuwei99986.presenter;

import android.os.Handler;
import android.util.Log;

import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRecordOption;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.livesdk.ILVChangeRoleRes;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.api.DataManager;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.MemberInfo;
import com.wta.NewCloudApp.jiuwei99986.model.SigData;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.views.customs.MembersDialogView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 小小程序员 on 2017/9/20.
 */

public class LiveHelp extends Presenter {
    private ChatView chatView;
    private CompositeSubscription compositeSubscription;
    private DataManager dataManager;
    private Handler handler;
    private MembersDialogView membersDialogView;
    private ArrayList<MemberInfo.DataEntity> members=new ArrayList<>();
    public void audienceApply(Map map) {
        compositeSubscription.add(dataManager.micApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MemberInfo>() {
                    @Override
                    public void call(MemberInfo data) {
                        parseData(data);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                }));
    }

    private void parseData(MemberInfo data) {
        if (membersDialogView!=null) {
            for (MemberInfo.DataEntity member : data.getData()) {
                MemberInfo.DataEntity item = new MemberInfo.DataEntity();
                item.setUserId(member.getUserId());
                item.setUserName(member.getUserName());
                AVVideoView avVideoView = ILiveRoomManager.getInstance().getRoomView().getUserAvVideoView(member.getUserId(), AVView.VIDEO_SRC_TYPE_CAMERA);
                if (null != avVideoView && avVideoView.isRendering()) {
                    item.setIsOnVideoChat(true);
                }
                members.add(item);
            }
            membersDialogView.showMembersList(members);
        }else {
            EventBus.getDefault().post(data);
        }

    }

    public LiveHelp() {
        compositeSubscription = new CompositeSubscription();
        dataManager = DataManager.getInstance();
    }

    public LiveHelp(MembersDialogView membersDialogView) {
        compositeSubscription = new CompositeSubscription();
        dataManager = DataManager.getInstance();
        this.membersDialogView = membersDialogView;
    }

    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    public void startExitRoom() {
        ILVLiveManager.getInstance().quitRoom(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });

    }

    public void startRecord(ILiveRecordOption option) {
        ILiveRoomManager.getInstance().startRecordVideo(option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                chatView.startRecordCallback(true);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                chatView.startRecordCallback(false);
            }
        });
    }

    public void stopRecord() {
        ILiveRoomManager.getInstance().stopRecordVideo(new ILiveCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                chatView.stopRecordCallback(true, data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                chatView.stopRecordCallback(false, null);
            }
        });
    }

    public int sendGroupCmd(int cmd, String param) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        return sendCmd(customCmd);
    }

    public int sendC2CCmd(final int cmd, String param, String destId) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setDestId(destId);
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eC2CMsg);
        return sendCmd(customCmd);
    }

    public void upMemberVideo() {
        if (!ILiveRoomManager.getInstance().isEnterRoom()) {
        }
        ILVLiveManager.getInstance().upToVideoMember(Constants.MIC_MEMBER_ROLE, true, true, new ILiveCallBack<ILVChangeRoleRes>() {
            @Override
            public void onSuccess(ILVChangeRoleRes data) {
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.i("TAG", "upToVideoMember->failed:" + module + "|" + errCode + "|" + errMsg);
            }
        });
    }

    public void downMemberVideo() {
        if (!ILiveRoomManager.getInstance().isEnterRoom()) {
        }
        ILVLiveManager.getInstance().downToNorMember(Constants.MIC_MEMBER_ROLE, new ILiveCallBack<ILVChangeRoleRes>() {
            @Override
            public void onSuccess(ILVChangeRoleRes data) {
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
            }
        });
    }

    private int sendCmd(final ILVCustomCmd cmd) {
        return ILVLiveManager.getInstance().sendCustomCmd(cmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
//                Toast.makeText(mContext, "sendCmd->failed:" + module + "|" + errCode + "|" + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createRoom(String liveId, String hostId) {
        ILiveRoomOption option = new ILiveRoomOption(hostId)
                .controlRole(Constants.HOST_ROLE)
                .autoCamera(true)
                .autoFocus(true)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)
                .highAudioQuality(true)
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)//权限设置
                .cameraId(ILiveConstants.FRONT_CAMERA)//摄像头前置后置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);//是否开始半自动接收
        option.imGroupId(liveId);
        ILiveRoomManager.getInstance().createRoom(Integer.parseInt(liveId), option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.i("TAG", "onSuccess:createRoom ");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.i("TAG", "createRoom: module=" + module + "  errCode=" + errCode + "  errMsg=" + errMsg);

            }
        });
    }

    public void joinRoom(String roomLiveId, String hostId) {
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(hostId)
                .autoCamera(false)
                .controlRole(Constants.NORMAL_MEMBER_ROLE)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)
                .autoSpeaker(true)
                .autoMic(false);
        ILVLiveManager.getInstance().joinRoom(Integer.parseInt(roomLiveId), memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (chatView != null) {
                    chatView.sendText(Constants.JOIN_ROOM);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errMsg.equals("this group does not exist")){
                    if (chatView != null) {
                        chatView.sendText(Constants.JOIN_ERROR);
                    }
                }
            }
        });
    }

    public void enterExitLive(Map map) {
        compositeSubscription.add(dataManager.enterLive(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SigData>() {
                    @Override
                    public void call(SigData data) {
                        if (data!=null){
                            data.setSign(1);
                        }
                        EventBus.getDefault().post(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("TAG", "call:throwable= "+throwable);
                    }
                }));
    }
    public void chat_record(Map map) {
        compositeSubscription.add(dataManager.chat_record(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData data) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("TAG", "call:throwable= "+throwable);
                    }
                }));
    }
    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        ILVLiveManager.getInstance().quitRoom(null);
    }

    public void loginTenCent(final int type, final String liveId, final String phoneNumber, final String sig) {
        ILiveLoginManager.getInstance().iLiveLogin(SharedPrefrenceManager.getInstance().getLiveNumber(), SharedPrefrenceManager.getInstance().getSig(), new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.i("TAG", "loginTenCent: ");
                if (type == 0) {
                    createRoom(liveId, SharedPrefrenceManager.getInstance().getLiveNumber());
                } else {

                    joinRoom(liveId, SharedPrefrenceManager.getInstance().getLiveNumber());
                }

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.i("TAG", "loginTenCent: module=" + module + "  errCode=" + errCode + "  errMsg=" + errMsg);

                reloginTenCent(type, liveId, phoneNumber, sig);
            }
        });
    }

    private void reloginTenCent(final int type, final String liveId, String phoneNumber, String sig) {
        SharedPrefrenceManager.getInstance().setLiveNumber(phoneNumber);
        SharedPrefrenceManager.getInstance().setSig(sig);
        ILiveLoginManager.getInstance().iLiveLogin(phoneNumber, sig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (type == 0) {
                    createRoom(liveId, SharedPrefrenceManager.getInstance().getLiveNumber());
                } else {

                    joinRoom(liveId, SharedPrefrenceManager.getInstance().getLiveNumber());
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }


}
