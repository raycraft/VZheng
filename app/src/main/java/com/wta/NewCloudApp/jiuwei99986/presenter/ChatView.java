package com.wta.NewCloudApp.jiuwei99986.presenter;

import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;

import java.util.List;

/**
 * 聊天界面的接口
 */
public interface ChatView  {

    /**
     * 显示消息
     */
    void showMessage(TIMMessage message);

    /**
     * 显示消息
     */
    void showMessage(List<TIMMessage> messages);


    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     * @param message 发送的消息
     */
    void onSendMessageFail(int code, String desc, TIMMessage message);

    void sendText(String msg);

    void showDraft(TIMMessageDraft draft);

    void startRecordCallback(boolean isSucc);

    void stopRecordCallback(boolean isSucc, List<String> files);
}
