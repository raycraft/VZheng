package com.wta.NewCloudApp.jiuwei99986.presenter;

import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.api.DataManager;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.ConsultData;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.LiveRoomMessage;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.model.SigData;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 小小程序员 on 2017/9/20.
 */

public class ConsultHelp extends Presenter {
    private CompositeSubscription compositeSubscription;
    private DataManager dataManager;
    private Map<String, String> dataMap = new HashMap<>();

    public ConsultHelp() {
        compositeSubscription = new CompositeSubscription();
        dataManager = DataManager.getInstance();
    }

    public void getMyLive() {
        dataMap.clear();
        dataMap.put("type", "consultant_room");
        dataMap.put(Constants.USERID, SharedPrefrenceManager.getInstance().getUserId());
        compositeSubscription.add(dataManager.getMyLive(dataMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LiveRoomMessage>() {
                    @Override
                    public void call(LiveRoomMessage findData) {
                        EventBus.getDefault().post(findData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());
                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }
                    }
                }));
    }

    public void getLiveList(Map map, final int page) {
        compositeSubscription.add(dataManager.getLiveList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ConsultData>() {
                    @Override
                    public void call(ConsultData data) {
                        if (data != null) {
                            data.setSign(page);
                        }
                        EventBus.getDefault().post(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());
                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }
                    }
                }));
    }

    public void modifyLiveRoom(Map map) {
        compositeSubscription.add(dataManager.modifyLiveRoom(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData data) {
                        EventBus.getDefault().post(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }));
    }

    public void ensureWord(Map map) {
        compositeSubscription.add(dataManager.enterLive(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SigData>() {
                    @Override
                    public void call(SigData data) {
                        if (data!=null){
                            data.setSign(0);
                        }
                        EventBus.getDefault().post(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                }));
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }
}
