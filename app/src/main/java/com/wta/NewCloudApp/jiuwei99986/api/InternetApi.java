package com.wta.NewCloudApp.jiuwei99986.api;

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

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by 小小程序员 on 2017/8/29.
 */

public interface InternetApi {
    @FormUrlEncoded
    @POST("forum_querylist.php")
    Observable<FindData> findData(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("forum_querylist.php")
    Observable<ScienceData> scienceData(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("forum_querylist.php")
    Observable<AskData> askData(@FieldMap Map<String, String> map);

    @Multipart
    @POST("forum_details.php")
    Observable<FindDetailData> findDetailData(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("forum_post.php")
    Observable<BaseData> replyPosts(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("space_list.php")
    Observable<MyMessage> modifyMessage(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("forum_upvote.php")
    Observable<FindDetailData> clickZan(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("login_app.php")
    Observable<LoginData> loginApi(@PartMap Map<String, RequestBody> params);


    @Multipart
    @POST("forum_thread.php")
    Observable<BaseData> followPost(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("forum_find.php")
    Observable<BaseData> publishPost(@PartMap Map<String, RequestBody> map);

    //问医生
    @Multipart
    @POST("forum_ask.php")
    Observable<BaseData> publishAsk(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("space_list.php")
    Observable<MyFindData> myFindData(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("space_list.php")
    Observable<FindData> myAskData(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("consulroom_modify.php")
    Observable<BaseData> modifyLiveRoom(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("login.php")
    Observable<WeiXinData> wexinCode(@FieldMap Map<String, String> map);

    //login.php
    @FormUrlEncoded
    @POST("space_list.php")
    Observable<LiveRoomMessage> getMyLive(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("advisory_querylist.php")
    Observable<ConsultData> getLiveList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("consulroom_inout.php")
    Observable<SigData> enterLive(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("consulroom_wheat.php")
    Observable<MemberInfo> micApi(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("chat_record.php")
    Observable<BaseData> chat_record(@FieldMap Map<String, String> map);
}
