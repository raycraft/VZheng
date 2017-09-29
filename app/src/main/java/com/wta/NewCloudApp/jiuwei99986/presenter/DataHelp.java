package com.wta.NewCloudApp.jiuwei99986.presenter;

import android.content.Context;
import android.util.Log;

import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.api.DataManager;
import com.wta.NewCloudApp.jiuwei99986.model.AskData;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.FindData;
import com.wta.NewCloudApp.jiuwei99986.model.FindDetailData;
import com.wta.NewCloudApp.jiuwei99986.model.LoginData;
import com.wta.NewCloudApp.jiuwei99986.model.MyFindData;
import com.wta.NewCloudApp.jiuwei99986.model.MyMessage;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.model.ScienceData;
import com.wta.NewCloudApp.jiuwei99986.model.WeiXinData;
import com.wta.NewCloudApp.jiuwei99986.utils.RxToast;

import java.net.ConnectException;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 小小程序员 on 2017/8/29.
 */

public class DataHelp extends Presenter {
    private CompositeSubscription compositeSubscription;
    private DataManager dataManager;

    public DataHelp(Context context) {
        compositeSubscription = new CompositeSubscription();
        dataManager = DataManager.getInstance();
    }

    public void findData(Map map, final int page) {
        compositeSubscription.add(dataManager.findData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FindData>() {
                    @Override
                    public void call(FindData findData) {
                        if (findData != null) {
                            findData.setTag(page);
                        }
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

    public void myFindData(Map map, final int page) {
        compositeSubscription.add(dataManager.myFindData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MyFindData>() {
                    @Override
                    public void call(MyFindData findData) {
                        if (findData != null) {
                            findData.setSign(page);
                        }
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
    public void myAskData(Map map, final int page) {
        compositeSubscription.add(dataManager.myAskData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FindData>() {
                    @Override
                    public void call(FindData findData) {
                        if (findData != null) {
                            findData.setSign(page);
                        }
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
    public void scienceData(Map map, final int sign) {
        compositeSubscription.add(dataManager.scienceData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ScienceData>() {
                    @Override
                    public void call(ScienceData findData) {
                        if (findData!=null){
                            findData.setSign(sign);
                        }
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

    public void askData(Map map, final int sign) {
        compositeSubscription.add(dataManager.askData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AskData>() {
                    @Override
                    public void call(AskData findData) {
                        if (findData != null) {
                            findData.setSign(sign);
                        }
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

    public void findDetailData(Map map, final int type) {
        compositeSubscription.add(dataManager.findDetailData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FindDetailData>() {
                    @Override
                    public void call(FindDetailData findData) {
                        if (findData != null) {
                            findData.setTag(type);
                        }
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

    public void clickZan(Map map) {
        compositeSubscription.add(dataManager.clickZan(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FindDetailData>() {
                    @Override
                    public void call(FindDetailData findData) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                       /* if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());
                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }*/
                    }
                }));
    }
    public void loginApi(Map map) {
        compositeSubscription.add(dataManager.loginApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoginData>() {
                    @Override
                    public void call(LoginData findData) {
                        EventBus.getDefault().post(findData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("TAG", "call: throwable="+throwable);
                       /* if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());
                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }*/
                    }
                }));
    }
    public void followPost(Map map) {
        compositeSubscription.add(dataManager.followPost(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData findData) {
                        if (findData != null) {
                            findData.setSign(1);
                        }
                        EventBus.getDefault().post(findData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                       /* if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());

                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }*/
                    }
                }));
    }

    public void replyPosts(Map map) {
        compositeSubscription.add(dataManager.replyPosts(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData findData) {
                        if (findData != null) {
                            findData.setSign(2);
                        }
                        EventBus.getDefault().post(findData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                       /* if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());

                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }*/
                    }
                }));
    }
    public void modifyMessage(Map map) {
        compositeSubscription.add(dataManager.modifyMessage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MyMessage>() {
                    @Override
                    public void call(MyMessage findData) {
                        EventBus.getDefault().post(findData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("TAG", "call: throwable="+throwable.toString());
                       /* if (throwable instanceof ConnectException) {
                            EventBus.getDefault().post(new NoNetWork());

                        } else {
                            EventBus.getDefault().post(new ErrorData());
                        }*/
                    }
                }));
    }
    public void publishPost(Map map) {
        compositeSubscription.add(dataManager.publishPost(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData findData) {

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

    public void publishAsk(Map map) {
        compositeSubscription.add(dataManager.publishAsk(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData>() {
                    @Override
                    public void call(BaseData findData) {

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
    public void wexinCode(Map map) {
        compositeSubscription.add(dataManager.wexinCode(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WeiXinData>() {
                    @Override
                    public void call(WeiXinData weiXinData) {

                        EventBus.getDefault().post(weiXinData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ConnectException) {
                            RxToast.normal(App.getContext().getResources().getString(R.string.view_network_error));
                        }
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
