package com.wta.NewCloudApp.jiuwei99986;

/**
 * Created by 小小程序员 on 2017/8/29.
 */

public class Constants {

    public static final String BASE_URL="http://www.weimeijieyuan.com/meiba/vzapi/";
    public static final String SIG_URL="http://www.weimeijieyuan.com:9001/wwwroot/meiba/vzapi/";
    //public static final String BASE_URL = "http://192.168.0.219:9001/wwwroot/meiba/vzapi/";
    public static final String WEB_URL="http://192.168.0.215/vzheng/science.html?tid=";
    //public static final String WEB_URL = "http://www.weimeijieyuan.com/vzheng/science.html?tid=";
    public static final String PAGE = "page";
    public static final String INTENT_DATA = "intent";
    public static final String FLAG = "ask";
    public static final String USERID = "userid";
    public static final String LIVE_ID = "liveid";
    public static final String PAGE_SIZE = "pagesize";
    public static final String PAGE_NUMBER = "20";
    public static final String TYPE = "type";
    public static final String ONE = "1";
    public static final String ZERO = "0";
    public static final String FLAG_FOUR = "1004";
    public static final String FLAG_SIX= "1006";
    public static final String MY_USERID = "310002";
    public static final String TID = "tid";
    public static final String FID = "fid";
    public static final String WEIXIN_APP_ID = "123";
    public static final String OBJECT = "object";
    public static final String SIGN_OBJECT = "sign_object";
    public static final String BROADCAST = "broadcast";
    public static final String WEIXIN_APPID = "wx6d196ea1c6eb36a7";
    public static final String WEIXIN_SECRET = "e75ae82c11998e0675c697b7c830df7b";
    //图片压缩大小
    public static final int SCALE_SIZE = 300;

    public static final int SDK_APPID = 1400021502;
    public static final int ACCOUNT_TYPE = 9814;
    /*直播*/
    public static final String ROOM_LIVEID = "room_liveid";
    public static final String ROOM_LIVEUSERID = "room_liveuserid";
    public static final String JOIN_ROOM = "join_room";
    public static final String ROOM_TAG = "room_tag";
    public static final String JOIN_ERROR = "join_error";
    public static final String CREATE_ROOM = "create_room";
    public static final int AVIMCMD_MULTI = 0x800;             // 多人互动消息类型

    public static final int AVIMCMD_MUlTI_HOST_INVITE = AVIMCMD_MULTI + 1;         // 邀请互动,
    public static final int AVIMCMD_MULTI_CANCEL_INTERACT = AVIMCMD_MUlTI_HOST_INVITE + 1;       // 断开互动，
    public static final int AVIMCMD_MUlTI_JOIN = AVIMCMD_MULTI_CANCEL_INTERACT + 1;       // 同意互动，
    public static final int AVIMCMD_MUlTI_REFUSE = AVIMCMD_MUlTI_JOIN + 1;      // 拒绝互动，

    public static final int AVIMCMD_MULTI_HOST_ENABLEINTERACTMIC = AVIMCMD_MUlTI_REFUSE + 1;  // 主播打开互动者Mic，
    public static final int AVIMCMD_MULTI_HOST_DISABLEINTERACTMIC = AVIMCMD_MULTI_HOST_ENABLEINTERACTMIC + 1;// 主播关闭互动者Mic，
    public static final int AVIMCMD_MULTI_HOST_ENABLEINTERACTCAMERA = AVIMCMD_MULTI_HOST_DISABLEINTERACTMIC + 1; // 主播打开互动者Camera，
    public static final int AVIMCMD_MULTI_HOST_DISABLEINTERACTCAMERA = AVIMCMD_MULTI_HOST_ENABLEINTERACTCAMERA + 1; // 主播打开互动者Camera
    public static final int AVIMCMD_MULTI_HOST_CANCELINVITE = AVIMCMD_MULTI_HOST_DISABLEINTERACTCAMERA + 1; //主播让某个互动者下麦
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_CAMERA = AVIMCMD_MULTI_HOST_CANCELINVITE + 1; //主播控制某个上麦成员摄像头
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_MIC = AVIMCMD_MULTI_HOST_CONTROLL_CAMERA + 1; //主播控制某个上麦成员MIC
    public static final int AVIMCMD_MULTI_HOST_SWITCH_CAMERA = AVIMCMD_MULTI_HOST_CONTROLL_MIC + 1; ////主播切换某个上麦成员MIC

    public static final int AVIMCMD_TEXT = -1;         // 普通的聊天消息
    public static final int AVIMCMD_NONE = AVIMCMD_TEXT + 1;               // 无事件
    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_ENTERLIVE = AVIMCMD_NONE + 1;          // 用户加入直播,
    public static final int AVIMCMD_EXITLIVE = AVIMCMD_ENTERLIVE + 1;         // 用户退出直播,
    public static final int AVIMCMD_PRAISE = AVIMCMD_EXITLIVE + 1;           // 点赞消息,
    public static final int AVIMCMD_HOST_LEAVE = AVIMCMD_PRAISE + 1;         // 主播离开,
    public static final int AVIMCMD_HOST_BACK = AVIMCMD_HOST_LEAVE + 1;      // 主播回来,
    public static final String CMD_KEY = "userAction";
    public static final String CMD_PARAM = "actionParam";
    public static final String HOST_ROLE = "mainliveand";
    public static final String VIDEO_MEMBER_ROLE = "LiveGuest";
    public static final String MIC_MEMBER_ROLE = "lmgzliveand";
    public static final String NORMAL_MEMBER_ROLE = "gzliveand";
    public static final String HOST_LEAVE = "$&|主播已经关闭直播";
    public static final String WATCHER_LEAVE = "&$&|";
    public static final String HOST = "host";


    //请求相机
    public static final int REQUEST_CAPTURE = 100;
    //请求相册
    public static final int REQUEST_PICK = 101;
    //请求截图
    public static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
}
