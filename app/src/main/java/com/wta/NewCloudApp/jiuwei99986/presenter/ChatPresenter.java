package com.wta.NewCloudApp.jiuwei99986.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.livesdk.ILVLiveConfig;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 聊天界面逻辑
 */
public class ChatPresenter implements Observer {

    private ChatView view;
    private TIMConversation conversation;
    private boolean isGetingMessage = false;
    private final int LAST_MESSAGE_NUM = 20;
    private final static String TAG = "ChatPresenter";
    private ILVLiveConfig config;

    public ChatPresenter(ILVLiveConfig config, ChatView view, String identify, TIMConversationType type) {
        this.view = view;
        conversation = TIMManager.getInstance().getConversation(type, identify);
        this.config = config;
        MessageEvent instance = MessageEvent.getInstance();
        instance.addObserver(this);
    }


    /**
     * 加载页面逻辑
     */
    public void start() {
        //注册消息监听
        // RefreshEvent.getInstance().addObserver(this);
        getMessage(null);
        if (conversation.hasDraft()) {
            view.showDraft(conversation.getDraft());
        }
    }


    /**
     * 中止页面逻辑
     */
    public void stop() {
        //注销消息监听
        MessageEvent.getInstance().deleteObserver(this);
        // RefreshEvent.getInstance().deleteObserver(this);
    }

    /**
     * 获取聊天TIM会话
     */
    public TIMConversation getConversation() {
        return conversation;
    }

    /**
     * 发送消息
     */
    public void sendMessage(String msg, String type, ILiveRoomManager manager, final Context context) {
        /*String nick = SharedPrefrenceManager.getInstance().getNick();
        if (nick == null) {
            nick = "匿名";
        }
        if (nick.trim().length() < 1) {
            nick = "匿名";
        }*/
        Log.i("TAG", "sendMessage: msg="+msg);
        TIMMessage Nmsg = new TIMMessage();
        Nmsg.setSender(SharedPrefrenceManager.getInstance().getUserId());
        TIMTextElem elem = new TIMTextElem();
        if (type.equals("-1")) {
            elem.setText(msg);
        } else {
            if (msg.length() == 0)
                return;
            try {
                byte[] byte_num = msg.getBytes("utf8");
                if (byte_num.length > 160) {
                    Toast.makeText(context, "发送的消息过长", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
            String newMessage = type + ":" + msg;

            elem.setText(newMessage);
        }
        if (Nmsg.addElement(elem) != 0) {
            return;
        }
        manager.sendGroupMessage(Nmsg, new ILiveCallBack<TIMMessage>() {
            @Override
            public void onSuccess(TIMMessage data) {
                Log.i("TAG", "TIMMessage: ");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.i("TAG", "onError: module="+module+"  errCode="+errCode+"  errMsg="+errMsg);

            }
        });

    }


    /**
     * 发送在线消息
     *
     * @param message 发送的消息
     */
    public void sendOnlineMessage(final TIMMessage message) {
        conversation.sendOnlineMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                view.onSendMessageFail(i, s, message);
            }

            @Override
            public void onSuccess(TIMMessage message) {

            }
        });
    }


    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        List<TIMMessage> list = (List<TIMMessage>) data;
        parseIMMessage(list);
        if (observable instanceof MessageEvent) {
            TIMMessage msg = (TIMMessage) data;
            if (msg == null || msg.getConversation().getPeer().equals(conversation.getPeer()) && msg.getConversation().getType() == conversation.getType()) {
                view.showMessage(msg);
                //当前聊天界面已读上报，用于多终端登录时未读消息数同步
                readMessages();
            }
        }
    }

    private void parseIMMessage(List<TIMMessage> list) {
        List<TIMMessage> tlist = list;

        for (int i = tlist.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = tlist.get(i);

            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null)
                    continue;
                TIMElem elem = currMsg.getElement(j);
                TIMElemType type = elem.getType();
                String sendId = currMsg.getSender();

                TIMUserProfile profile = currMsg.getSenderProfile();
                //最后处理文本消息
                if (type == TIMElemType.Text) {
                    if (currMsg.isSelf()) {
                        handleTextMessage(elem, "123");
                    } else {
                        String nickname;
                        if (currMsg.getSenderProfile() != null && (!currMsg.getSenderProfile().getNickName().equals(""))) {
                            nickname = currMsg.getSenderProfile().getNickName();
                        } else {
                            nickname = sendId;
                        }
                        handleTextMessage(elem, nickname);
                    }
                }
            }
        }
    }

    private void handleTextMessage(TIMElem elem, String name) {
        TIMTextElem textElem = (TIMTextElem) elem;

        // mLiveView.refreshText(textElem.getText(), name);
    }

    /**
     * 获取消息
     *
     * @param message 最后一条消息
     */
    public void getMessage(@Nullable TIMMessage message) {
        if (!isGetingMessage) {
            isGetingMessage = true;
            conversation.getMessage(LAST_MESSAGE_NUM, message, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    isGetingMessage = false;
                    Log.e(TAG, "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    isGetingMessage = false;
                    view.showMessage(timMessages);
                }
            });
        }

    }

    /**
     * 设置会话为已读
     */
    public void readMessages() {
        conversation.setReadMessage();
    }


    /**
     * 保存草稿
     *
     * @param message 消息数据
     */
    public void saveDraft(TIMMessage message) {
        conversation.setDraft(null);
        if (message != null && message.getElementCount() > 0) {
            TIMMessageDraft draft = new TIMMessageDraft();
            for (int i = 0; i < message.getElementCount(); ++i) {
                draft.addElem(message.getElement(i));
            }
            conversation.setDraft(draft);
        }

    }


}
