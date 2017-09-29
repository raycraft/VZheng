package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.adapter.FindContentAdapter;
import com.wta.NewCloudApp.jiuwei99986.adapter.SmallPhotoAdapter;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.listener.OnAdapterItemClickListener;
import com.wta.NewCloudApp.jiuwei99986.listener.OnItemSelectListener;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.Compress;
import com.wta.NewCloudApp.jiuwei99986.model.ErrorData;
import com.wta.NewCloudApp.jiuwei99986.model.FindDetailData;
import com.wta.NewCloudApp.jiuwei99986.model.NoNetWork;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.CommonUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.DensityUtil;
import com.wta.NewCloudApp.jiuwei99986.utils.PictureUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.ProgressDialogUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.utils.Utils;
import com.wta.NewCloudApp.jiuwei99986.widget.CommentList;
import com.wta.NewCloudApp.jiuwei99986.widget.ExpandTextView;
import com.wta.NewCloudApp.jiuwei99986.widget.GlideCircleTransform;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 小小程序员 on 2017/8/30.
 */

public class FindContentActivity extends BaseActivity implements View.OnLayoutChangeListener, OnItemSelectListener {
    private SmartRefreshLayout smartRefreshLayout;
    private FindDetailData.DataEntity.CommentEntity entity;
    private RecyclerView recyclerView;
    private List<FindDetailData.DataEntity.CommentEntity> list;
    private View headView;
    private FindContentAdapter findContentAdapter;
    private DataHelp dataHelp;
    private Map<String, RequestBody> dataMap = new HashMap<>();
    private Map<String, RequestBody> map = new HashMap<>();
    private Map<String, RequestBody> postMap = new HashMap<>();
    @BindView(R.id.tool_content)
    TextView title;
    private ImageView head;
    private int currentPage = 1;
    private TextView name, time, sort, replyForum, photo, send;
    private GridLayoutManager chatManage;
    private LinearLayout linearLayout, commnetBody, sendBody;
    private ExpandTextView expandTextView;
    private List<String> chats = new ArrayList<>();
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    @BindView(R.id.image_recycle)
    RecyclerView imageRecycle;
    @BindView(R.id.forum_send_content)
    EditText sendEditext;
    @BindView(R.id.reply_edittext)
    EditText commentEditext;
    @BindView(R.id.container)
    LinearLayout bodyLayout;
    private List<File> listFiles = new ArrayList<>();
    //isReply:是否跟帖 isDirect 是否直接回复跟帖人还是跟帖人下面回复的人 isfollow:判断是否需要滚动recycle
    private boolean isZan, isreply, isDirect, isFollow;
    private String zan, Zan, userId, fid, tid, postId;
    //position 跟帖位置  itemPosition跟帖内回复的位置
    private int zanS, keyHeight, position, itemPosition;
    private BaseQuickAdapter chatAdapter;
    private SmallPhotoAdapter photoAdapter;
    private LinearLayoutManager layoutManager;
    private int currentKeyboardH;
    private int screenHeight;
    private int editTextBodyHeight;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    @Override
    protected int getLayoutId() {
        return R.layout.find_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        showLoadingView();
        chats = Arrays.asList(getResources().getStringArray(R.array.emoji));
        dataHelp = new DataHelp(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        headView = LayoutInflater.from(this).inflate(R.layout.find_content_head, null);
        head = headView.findViewById(R.id.headIv);
        name = headView.findViewById(R.id.find_comment_name);
        time = headView.findViewById(R.id.find_comment_time);
        sort = headView.findViewById(R.id.find_content_sort);
        tid = getIntent().getStringExtra(Constants.INTENT_DATA);
        fid = getIntent().getStringExtra(Constants.FLAG);

        linearLayout = headView.findViewById(R.id.find_comment_linearLayout);
        commnetBody = (LinearLayout) findViewById(R.id.edit_comment);
        sendBody = (LinearLayout) findViewById(R.id.forum_send_body);
        expandTextView = headView.findViewById(R.id.find_content_view);
        layoutManager = new LinearLayoutManager(this);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh);
        recyclerView.setLayoutManager(layoutManager);
        findContentAdapter = new FindContentAdapter(fid, R.layout.forum_comment_item, list, this, dataHelp);
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                parseData(currentPage);
            }
        });
        parseData(currentPage);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 1;
                parseData(currentPage);
            }
        });
        recyclerView.setAdapter(findContentAdapter);
        findContentAdapter.addHeaderView(headView);
        initData();
        setViewTreeObserver();
    }

    private void initData() {
        photoAdapter = new SmallPhotoAdapter(this, selectedPhotos, this);
        chatAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.chat_image, chats) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView image = helper.getView(R.id.chatEmojy);
                final String[] text = item.split(",");
                String fileName = text[0].substring(0, text[0].lastIndexOf("."));
                int resID = getResources().getIdentifier(fileName,
                        "drawable", getPackageName());
                if (resID == 0) {
                    resID = getResources().getIdentifier("emoji_0",
                            "drawable", getPackageName());
                }
                image.setImageResource(resID);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendEditext.setText(Utils.getInstace().getExpressionString(App.getContext(), sendEditext.getText().toString() + text[1]));
                        sendEditext.setSelection(sendEditext.getText().length());
                    }
                });
            }


        };
        findContentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int iPosition) {
                if (!SharedPrefrenceManager.getInstance().getIsLogin()){
                    Toast.makeText(FindContentActivity.this, getResources().getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FindContentActivity.this,ActivityLogin.class));
                    return;
                }
                position = iPosition;
                isDirect = false;
                entity = (FindDetailData.DataEntity.CommentEntity) adapter.getItem(position);
                updateEditTextBodyVisible(View.VISIBLE, true);

            }
        });
        findContentAdapter.setOnCommentListListener(new OnAdapterItemClickListener() {
            @Override
            public void onItemClick(FindDetailData.DataEntity.CommentEntity item, int Position, int iPosition) {
                if (!SharedPrefrenceManager.getInstance().getIsLogin()){
                    Toast.makeText(FindContentActivity.this, getResources().getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FindContentActivity.this,ActivityLogin.class));
                    return;
                }
                isDirect = true;
                entity = item;
                itemPosition = Position;
                position = iPosition - 1;
                entity = findContentAdapter.getItem(position);
                updateEditTextBodyVisible(View.VISIBLE, true);

            }
        });
        keyHeight = getWindowManager().getDefaultDisplay().getHeight() / 10;
        replyForum = (TextView) findViewById(R.id.comment_praise);
        photo = (TextView) findViewById(R.id.forum_camera);
        commentEditext = (EditText) findViewById(R.id.reply_edittext);
        commentEditext.setFocusable(false);
        commentEditext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPrefrenceManager.getInstance().getIsLogin()){
                    Toast.makeText(FindContentActivity.this, getResources().getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FindContentActivity.this,ActivityLogin.class));
                    return;
                }
                isreply = false;
                sendEditext.requestFocus();
                photo.setVisibility(View.VISIBLE);
                sendBody.setVisibility(View.VISIBLE);
                commnetBody.setVisibility(View.GONE);
                CommonUtils.showSoftInput(sendEditext.getContext(), sendEditext);
            }
        });
        replyForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPrefrenceManager.getInstance().getIsLogin()){
                    Toast.makeText(FindContentActivity.this, getResources().getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FindContentActivity.this,ActivityLogin.class));
                    return;
                }
                if (isZan || Zan.equals("1")) {
                    isZan = true;
                    Toast.makeText(FindContentActivity.this, "您已经点过赞了", Toast.LENGTH_SHORT).show();
                    return;
                }
                Drawable drawable = getResources().getDrawable(R.drawable.praise_select);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                replyForum.setCompoundDrawables(null, null, drawable, null);
                if (!zan.matches("^\\d+$")){
                    zan="0";
                }
                replyForum.setText(Integer.parseInt(zan) + 1 + "");
                zanS++;
                isZan = true;
                map.clear();
                map.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
                map.put("tid", RequestBody.create(MediaType.parse("text/plain"), tid));
                map.put("Thread", RequestBody.create(MediaType.parse("text/plain"), "1"));
                map.put("pid", RequestBody.create(MediaType.parse("text/plain"), "1"));
               /* if (fid != null) {
                    *//*map.put("type", "ask");
                    map.put("fid", fid);*//*
                    map.put("type", RequestBody.create(MediaType.parse("text/plain"), "ask"));
                    map.put("fid", RequestBody.create(MediaType.parse("text/plain"), fid));

                }*/
                dataHelp.clickZan(map);
            }

        });

    }

    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }
                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = sendBody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    //updateEditTextBodyVisible(View.GONE, isreply);
                    return;
                }
                //偏移listview
                if (layoutManager != null && entity != null) {
                    layoutManager.scrollToPositionWithOffset(position + 1, getListViewOffset(entity));

                   /* if (isDirect){
                    }else {
                        layoutManager.scrollToPositionWithOffset(position+1, getListViewOffset(entity));
                    }*/
                }
            }
        });
    }

    private int getListViewOffset(FindDetailData.DataEntity.CommentEntity commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - DensityUtil.dip2px(App.getContext(), 38);
        if (isDirect) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(FindDetailData.DataEntity.CommentEntity commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(position + 1 - firstPosition);
        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (isDirect) {
            if (selectCircleItem == null) {
                return;
            }
            //回复评论的情况
            CommentList commentLv = (CommentList) selectCircleItem.findViewById(R.id.comment_list);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(itemPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();

                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);

                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //是否弹出键盘
    private void updateEditTextBodyVisible(int visibility, boolean reply) {
        isreply = reply;
        measureCircleItemHighAndCommentItemOffset(entity);
        if (View.VISIBLE == visibility) {
            sendBody.setVisibility(View.VISIBLE);
            photo.setVisibility(View.GONE);
            sendEditext.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(sendEditext.getContext(), sendEditext);
            commnetBody.setVisibility(View.GONE);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(sendEditext.getContext(), sendEditext);
            if (commnetBody != null) {
                sendBody.setVisibility(View.GONE);
                imageRecycle.setVisibility(View.GONE);
                commnetBody.setVisibility(View.VISIBLE);
            }
        }
    }

    //初始化表情
    private void initChatRecycle() {
        chatManage = new GridLayoutManager(this, 7);
        imageRecycle.setLayoutManager(chatManage);
        imageRecycle.setAdapter(chatAdapter);
    }

    private void parseData(int page) {
        dataMap.clear();
        if (SharedPrefrenceManager.getInstance().getIsLogin()){
            dataMap.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));

        }
        dataMap.put(Constants.PAGE, RequestBody.create(MediaType.parse("text/plain"), page + ""));
        dataMap.put(Constants.PAGE_SIZE, RequestBody.create(MediaType.parse("text/plain"),"10"));
        dataMap.put("tid", RequestBody.create(MediaType.parse("text/plain"), tid));

        if (fid != null) {
            dataMap.put("type", RequestBody.create(MediaType.parse("text/plain"),  "ask"));
        }
        dataHelp.findDetailData(dataMap, page);
    }


    //跟帖
    private void followPost() {
        isFollow = true;
        postMap.clear();
        postMap.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
        postMap.put(Constants.TID, RequestBody.create(MediaType.parse("text/plain"), tid));
        postMap.put("info", RequestBody.create(MediaType.parse("text/plain"), sendEditext.getText().toString()));
        /*if (fid != null) {
            postMap.put(Constants.TYPE, RequestBody.create(MediaType.parse("text/plain"), Constants.FLAG));
            postMap.put(Constants.FID, RequestBody.create(MediaType.parse("text/plain"), fid));

        }*/
        for (File file : listFiles) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            postMap.put("fileUpload[]\"; filename=\"" + file.getName() , fileBody);
        }
        dataHelp.followPost(postMap);

    }

    private void deleteFiles() {
        for (int i = 0; i < listFiles.size(); i++) {
            if (listFiles.get(i).exists()) {
                listFiles.get(i).delete();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void isCompressed(Compress co) {
        followPost();
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void followResult(BaseData data) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (data == null) {
            Toast.makeText(FindContentActivity.this, getResources().getString(R.string.toast_post), Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getFlag().equals("0")) {
            Toast.makeText(FindContentActivity.this, getResources().getString(R.string.toast_post), Toast.LENGTH_SHORT).show();
        } else {
            if (data.getSign() == 1) {
                sendEditext.setText("");
                dataMap.clear();
                deleteFiles();
                listFiles.clear();
                selectedPhotos.clear();
                parseData(1);
            } else {
                addLocalReply();
            }

        }
    }

    private void addLocalReply() {
        //首先填充临时数据
        FindDetailData.DataEntity.CommentEntity.ReplyEntity replyEntity = new FindDetailData.DataEntity.CommentEntity.ReplyEntity();
        if (!isDirect) {
            entity = (FindDetailData.DataEntity.CommentEntity) findContentAdapter.getItem(position);
            replyEntity.setRemembername(entity.getMembername());
        } else {
            replyEntity.setRemembername(entity.getSon().get(itemPosition).getMembername());

        }
        replyEntity.setContent(sendEditext.getText().toString());
        replyEntity.setMembername(SharedPrefrenceManager.getInstance().getNick());
        List<FindDetailData.DataEntity.CommentEntity.ReplyEntity> son = entity.getSon();
        if (son == null) {
            son = new ArrayList<>();
            entity.setSon(son);
        }
        son.add(replyEntity);
        findContentAdapter.notifyDataSetChanged();
        sendEditext.setText("");
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void detailData(FindDetailData data) {
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
        }
        if (smartRefreshLayout.isLoading()) {
            smartRefreshLayout.finishLoadmore();
        }
        if (data == null) {
            showEmptyView();
            return;
        }
        showContentView();
        Zan = data.getData().getZa();
        zan=data.getData().getUpvotes();

       /* if (fid==null){
        }else {
            zan = data.getData().getHotreplys();
        }*/
        if (data.getTag() == 1) {
            if (!isFollow) {
                name.setText(data.getData().getMembername());
                time.setText(data.getData().getDateline());
                expandTextView.setText(data.getData().getContent());
                Glide.with(this).load(data.getData().getMemberphoto()).transform(new GlideCircleTransform(this)).into(head);
                addViews(data.getData().getPics());
                name.setText(data.getData().getMembername());
                if (Zan.equals(Constants.ONE)) {
                    Drawable drawable = getResources().getDrawable(R.drawable.praise_select);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    replyForum.setCompoundDrawables(null, null, drawable, null);
                }
                replyForum.setText(zan);
            }
            findContentAdapter.getData().clear();
            findContentAdapter.notifyDataSetChanged();
        }
        if (data.getData().getComment() != null) {
            findContentAdapter.addData(data.getData().getComment());
            if (isFollow) {
                recyclerView.scrollToPosition(1);
                recyclerView.requestLayout();
            }
        } else {
            smartRefreshLayout.setLoadmoreFinished(true);
        }

    }

    private void addViews(List<FindDetailData.DataEntity.Pics> pics) {
        linearLayout.removeAllViews();
        if (pics == null) {
            return;
        }
        for (int i = 0; i < pics.size(); i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = DensityUtil.dip2px(this, 10);
            imageView.setLayoutParams(params);
            Glide.with(this).load(pics.get(i).getPic()).into(imageView);
            linearLayout.addView(imageView);
        }
    }

    ProgressDialog progressDialog;

    @OnClick({R.id.forum_camera, R.id.forum_send, R.id.forum_chat, R.id.forum_close, R.id.forum_send_content, R.id.toolbar_back})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.forum_camera:
                CommonUtils.hideSoftInput(sendEditext.getContext(), sendEditext);
                if (imageRecycle.getVisibility() == View.GONE) {
                    initPhotoRecycle();
                    imageRecycle.setVisibility(View.VISIBLE);
                } else {
                    imageRecycle.setVisibility(View.GONE);
                }
                break;
            case R.id.forum_send:
                if (TextUtils.isEmpty(sendEditext.getText().toString().trim())) {
                    Toast.makeText(FindContentActivity.this, getResources().getString(R.string.empty_content), Toast.LENGTH_SHORT).show();
                    return;
                }
                //回复
                if (isreply) {
                    replyPost();
                } else {
                    //跟帖
                    if (selectedPhotos.size() > 0) {
                        progressDialog = ProgressDialogUtils.showProgressDialog(FindContentActivity.this, "正在上传");
                        compressPhoto();
                    } else {
                        followPost();
                    }

                }
                updateEditTextBodyVisible(View.GONE, false);
                break;
            case R.id.forum_chat:
                CommonUtils.hideSoftInput(sendEditext.getContext(), sendEditext);
                if (imageRecycle.getVisibility() == View.GONE) {
                    initChatRecycle();
                    imageRecycle.setVisibility(View.VISIBLE);
                } else {
                    imageRecycle.setVisibility(View.GONE);
                }
                break;
            case R.id.forum_close:
                CommonUtils.hideSoftInput(sendEditext.getContext(), sendEditext);
                sendBody.setVisibility(View.GONE);
                commnetBody.setVisibility(View.VISIBLE);
                imageRecycle.setVisibility(View.GONE);
                break;
            case R.id.forum_send_content:
                imageRecycle.setVisibility(View.GONE);
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    //跟帖回复
    private void replyPost() {
        postMap.clear();
        postMap.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
        postMap.put(Constants.TID, RequestBody.create(MediaType.parse("text/plain"), tid));
        postMap.put("rememberid", RequestBody.create(MediaType.parse("text/plain"), entity.getMemberid()));
        postMap.put("remembername", RequestBody.create(MediaType.parse("text/plain"), entity.getMembername()==null?"":entity.getMembername()));
        postMap.put("info", RequestBody.create(MediaType.parse("text/plain"), sendEditext.getText().toString()));
        postMap.put("pid", RequestBody.create(MediaType.parse("text/plain"), entity.getId()));

      /*  if (fid != null) {
            postMap.put(Constants.FID, RequestBody.create(MediaType.parse("text/plain"), fid));
            postMap.put(Constants.TYPE, RequestBody.create(MediaType.parse("text/plain"), Constants.FLAG));

        }*/
        dataHelp.replyPosts(postMap);
    }

    //压缩图片
    private void compressPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < selectedPhotos.size(); i++) {
                    if (i == selectedPhotos.size() - 1) {
                        listFiles.add(PictureUtils.getImage(selectedPhotos.get(i), true, 200));
                    } else {
                        listFiles.add(PictureUtils.getImage(selectedPhotos.get(i), false, 200));
                    }
                }
            }
        }).start();
    }

    private void initPhotoRecycle() {
        imageRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageRecycle.setAdapter(photoAdapter);
    }

    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int i4, int i5, int i6, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/10屏幕高，就认为软键盘弹起
        //软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            replyForum.setText("");
            imageRecycle.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(R.drawable.comment_send);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            replyForum.setCompoundDrawables(null, null, drawable, null);

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getError(ErrorData data) {
        super.getError(data);
        showErrorView();
    }

    @Override
    public void getErrorData(NoNetWork data) {
        super.getErrorData(data);
        showNoNetworkView();
    }

    @Override
    public void onNetworkViewRefresh() {
        super.onNetworkViewRefresh();
        parseData(currentPage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showContentView();
        dataHelp.onDestroy();
    }

    @Override
    public void onTabSelect(int position) {
        selectedPhotos.remove(position);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPictureSelect(int position) {
        if (photoAdapter.getItemViewType(position) == SmallPhotoAdapter.TYPE_ADD) {
            PhotoPicker.builder()
                    .setPhotoCount(SmallPhotoAdapter.MAX)
                    .setShowCamera(true)
                    .setPreviewEnabled(false)
                    .setSelected(selectedPhotos)
                    .start(FindContentActivity.this);
        } else {
            PhotoPreview.builder()
                    .setPhotos(selectedPhotos)
                    .setCurrentItem(position)
                    .start(FindContentActivity.this);
        }
    }
}
