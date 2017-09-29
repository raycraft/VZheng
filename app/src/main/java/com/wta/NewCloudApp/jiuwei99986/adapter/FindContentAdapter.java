package com.wta.NewCloudApp.jiuwei99986.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.listener.OnAdapterItemClickListener;
import com.wta.NewCloudApp.jiuwei99986.model.FindDetailData;
import com.wta.NewCloudApp.jiuwei99986.model.PhotoInfo;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.utils.Utils;
import com.wta.NewCloudApp.jiuwei99986.views.activity.ActivityLogin;
import com.wta.NewCloudApp.jiuwei99986.views.activity.FindContentActivity;
import com.wta.NewCloudApp.jiuwei99986.views.activity.ImagePagerActivity;
import com.wta.NewCloudApp.jiuwei99986.widget.CommentList;
import com.wta.NewCloudApp.jiuwei99986.widget.ExpandTextView;
import com.wta.NewCloudApp.jiuwei99986.widget.GlideCircleTransform;
import com.wta.NewCloudApp.jiuwei99986.widget.MultiImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 小小程序员 on 2017/8/31.
 */

public class FindContentAdapter extends BaseQuickAdapter<FindDetailData.DataEntity.CommentEntity, BaseViewHolder> {
    private Context context;
    private List<Integer> zanPosition = new ArrayList<>();
    public List<PhotoInfo> photoInfos = new ArrayList<>();
    private DataHelp dataHelp;
    private String isAsk;
    private Map<String, RequestBody> map = new HashMap<>();
    private OnAdapterItemClickListener itemListener;

    public FindContentAdapter(String isAsk, @LayoutRes int layoutResId, @Nullable List<FindDetailData.DataEntity.CommentEntity> data, Context context, DataHelp dataHelp) {
        super(layoutResId, data);
        this.context = context;
        this.dataHelp = dataHelp;
        this.isAsk = isAsk;
    }

    public void setOnCommentListListener(OnAdapterItemClickListener listener) {
        itemListener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FindDetailData.DataEntity.CommentEntity item) {
        helper.setText(R.id.find_comment_name, item.getMembername())
                .setText(R.id.find_comment_time, item.getDateline())
                .setText(R.id.find_comment_agree, item.getUpvotes())
                .addOnClickListener(R.id.find_item_content);

        ImageView imageView = helper.getView(R.id.headIv);
        final TextView praise = helper.getView(R.id.find_comment_agree);
        Glide.with(context).load(item.getMemberphoto()).transform(new GlideCircleTransform(context)).into(imageView);
        ExpandTextView expandTextView = helper.getView(R.id.find_item_content);
        expandTextView.setText(Utils.getInstace().getExpressionString(App.getContext(), item.getContent()));
        MultiImageView multiImageView = helper.getView(R.id.multiImagView);
        multiImageView.setCanClick(true);
        if (item.getPics() != null && item.getPics().size() > 0) {
            photoInfos.clear();
            for (String url : item.getPics()) {
                PhotoInfo photoInfo1 = new PhotoInfo();
                photoInfo1.url = url;
                photoInfo1.w = 240;
                photoInfo1.h = 282;
                photoInfos.add(photoInfo1);
            }
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(photoInfos);
            multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //imagesize是作为loading时的图片size
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                    List<String> photoUrls = new ArrayList<String>();
                    for(PhotoInfo photoInfo : photoInfos){
                        photoUrls.add(photoInfo.url);
                    }
                    ImagePagerActivity.startImagePagerActivity(((FindContentActivity) context), photoUrls, position, imageSize);


                }
            });
        } else {
            multiImageView.setVisibility(View.GONE);
        }
        //处理回复
        CommentList commentList = helper.getView(R.id.comment_list);
        commentList.setOnItemClickListener(new CommentList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (itemListener != null) {
                    itemListener.onItemClick(item, position,helper.getLayoutPosition());
                }
            }
        });
        if (item.getSon() != null) {
            commentList.setDatas(item.getSon());
            helper.setVisible(R.id.comment_list, true);

        } else {
            helper.setVisible(R.id.comment_list, false);
        }
        if (item.getZa().equals(Constants.ONE) || zanPosition.contains(helper.getLayoutPosition())) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.praise_select);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            praise.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.praise);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            praise.setCompoundDrawables(null, null, drawable, null);
        }
        praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPrefrenceManager.getInstance().getIsLogin()){
                    Toast.makeText(context, context.getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context,ActivityLogin.class));
                    return;
                }
                if (item.getZa().equals(Constants.ONE)) {
                    Toast.makeText(context, "您已点过赞了", Toast.LENGTH_SHORT).show();
                } else {
                    zanPosition.add(helper.getLayoutPosition());
                    praise.setText(Integer.parseInt(item.getUpvotes()) + 1 + "");
                    Drawable drawable = context.getResources().getDrawable(R.drawable.praise_select);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    praise.setCompoundDrawables(null, null, drawable, null);
                    item.setZa("1");
                    /*map.clear();
                    map.put("tid", item.getTid());
                    map.put("pid", item.getId());
                    map.put("Thread", "0");
                    map.put(Constants.USERID, SharedPrefrenceManager.getInstance().getUserId());
                    if (isAsk != null) {
                        map.put("type", "ask");
                        map.put("fid", isAsk);
                    }
*/
                    map.clear();
                /*map.put("tid", tid);
                map.put("Thread", "1");
                map.put("pid", "1");*/

                    map.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
                    map.put("tid", RequestBody.create(MediaType.parse("text/plain"), item.getTid()));
                    map.put("Thread", RequestBody.create(MediaType.parse("text/plain"), "0"));
                    map.put("pid", RequestBody.create(MediaType.parse("text/plain"), item.getId()));

                    /*if (isAsk != null) {
                    *//*map.put("type", "ask");
                    map.put("fid", fid);*//*
                        map.put("type", RequestBody.create(MediaType.parse("text/plain"), "ask"));
                        map.put("fid", RequestBody.create(MediaType.parse("text/plain"), isAsk));

                    }*/
                    dataHelp.clickZan(map);
                }
            }
        });
    }
}
