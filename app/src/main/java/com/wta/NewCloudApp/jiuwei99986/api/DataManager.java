package com.wta.NewCloudApp.jiuwei99986.api;


import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.model.AskData;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.ConsultData;
import com.wta.NewCloudApp.jiuwei99986.model.FindData;
import com.wta.NewCloudApp.jiuwei99986.model.FindDetailData;
import com.wta.NewCloudApp.jiuwei99986.model.LiveRoomMessage;
import com.wta.NewCloudApp.jiuwei99986.model.LoginData;
import com.wta.NewCloudApp.jiuwei99986.model.MemberInfo;
import com.wta.NewCloudApp.jiuwei99986.model.MyFindData;
import com.wta.NewCloudApp.jiuwei99986.model.MyMessage;
import com.wta.NewCloudApp.jiuwei99986.model.ScienceData;
import com.wta.NewCloudApp.jiuwei99986.model.SigData;
import com.wta.NewCloudApp.jiuwei99986.model.WeiXinData;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by 小小程序员 on 2017/8/29.
 */

public class DataManager {
    private static DataManager ourInstance=new DataManager();
    private InternetApi internetApi,sigApi;
    public static DataManager getInstance() {
        if (ourInstance==null){
            synchronized (DataManager.class){
                if (ourInstance==null){
                    ourInstance=new DataManager();
                }
            }
        }
        return ourInstance;
    }
    private DataManager(){
        Retrofit baseRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120,TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build())
                .build();
        internetApi=baseRetrofit.create(InternetApi.class);

        Retrofit sigRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.SIG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120,TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build())
                .build();
        sigApi=sigRetrofit.create(InternetApi.class);
    }
   public Observable<FindData> findData(Map<String,String> map){
       return internetApi.findData(map);
   }
    public Observable<LiveRoomMessage> getMyLive(Map<String,String> map){
        return internetApi.getMyLive(map);
    }
    public Observable<ConsultData> getLiveList(Map<String,String> map){
        return internetApi.getLiveList(map);
    }
    public Observable<SigData> enterLive(Map<String,String> map){
        return sigApi.enterLive(map);
    }
    public Observable<MemberInfo> micApi(Map<String,String> map){
        return internetApi.micApi(map);
    }
    public Observable<BaseData> chat_record(Map<String,String> map){
        return internetApi.chat_record(map);
    }
    public Observable<MyFindData> myFindData(Map<String,RequestBody> map){
        return internetApi.myFindData(map);
    }
    public Observable<FindData> myAskData(Map<String,RequestBody> map){
        return internetApi.myAskData(map);
    }
    public Observable<BaseData> modifyLiveRoom(Map<String,RequestBody> map){
        return internetApi.modifyLiveRoom(map);
    }
    public Observable<WeiXinData> wexinCode(Map<String,String> map){
        return internetApi.wexinCode(map);
    }
    public Observable<ScienceData> scienceData(Map<String,String> map){
        return internetApi.scienceData(map);
    }
    public Observable<AskData> askData(Map<String,String> map){
        return internetApi.askData(map);
    }
    public Observable<FindDetailData> findDetailData(Map<String,RequestBody> map){
        return internetApi.findDetailData(map);
    }
    public Observable<BaseData> replyPosts(Map<String, RequestBody> map){
        return internetApi.replyPosts(map);
    }
    public Observable<MyMessage> modifyMessage(Map<String, RequestBody> map){
        return internetApi.modifyMessage(map);
    }
    //modifyMessage
    public Observable<FindDetailData> clickZan(Map<String, RequestBody> map){
        return internetApi.clickZan(map);
    }
    public Observable<LoginData> loginApi(Map<String, RequestBody> map){
        return internetApi.loginApi(map);
    }
    public Observable<BaseData> publishPost(Map<String, RequestBody> map){
        return internetApi.publishPost(map);
    }
    public Observable<BaseData> publishAsk(Map<String, RequestBody> map){
        return internetApi.publishAsk(map);
    }
//publishPost
    public Observable<BaseData> followPost (Map<String, RequestBody> map){
        return internetApi.followPost(map);
    }
//findDetailData
}
