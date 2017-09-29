package com.wta.NewCloudApp.jiuwei99986.presenter;


import java.util.Observable;

/**
 * 消息通知事件，上层界面可以订阅此事件
 */
public class MessageEvent extends Observable {


    private volatile static MessageEvent instance;
    public static MessageEvent getInstance(){
        if (instance == null) {
            synchronized (MessageEvent.class) {
                if (instance == null) {
                    instance = new MessageEvent();
                }
            }
        }
        return instance;
    }
    public void setDatas(){
        setChanged();
    }
    /**
     * 清理消息监听
     */
    public void clear(){
        instance = null;
    }



}
