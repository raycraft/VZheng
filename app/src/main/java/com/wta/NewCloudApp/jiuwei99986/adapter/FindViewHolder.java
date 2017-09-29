package com.wta.NewCloudApp.jiuwei99986.adapter;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.wta.NewCloudApp.jiuwei99986.model.PhotoInfo;
import com.wta.NewCloudApp.jiuwei99986.widget.ExpandTextView;
import com.wta.NewCloudApp.jiuwei99986.widget.MultiImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yiw on 2016/8/16.
 */
public abstract class FindViewHolder extends BaseViewHolder {

    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;

    public int viewType;
    public ImageView headIv,vip;
    public TextView nameTv;
    public TextView grade;
    public LinearLayout all;
    /** 动态的内容 */
    public ExpandTextView contentTv;
    public TextView timeTv;
    public TextView watchNumble,commentNumble,loveNumble,sort;
    /** 点赞列表*/
    public List<String> photos;
    public List<PhotoInfo> photoInfos;
    public MultiImageView multiImageView;
    /** 评论列表 */
    // ===========================

    public FindViewHolder(View itemView) {
        super(itemView);

       /* ViewStub viewStub =  itemView.findViewById(R.id.viewStub);
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView =  subView.findViewById(R.id.multiImagView);
        if(multiImageView != null){
            this.multiImageView = multiImageView;
        }
        initSubView(viewType, viewStub);*/

      /*  headIv = itemView.findViewById(R.id.headIv);
        grade =  itemView.findViewById(R.id.forum_grade);
        sort =  itemView.findViewById(R.id.forum_item_sort);
        nameTv =  itemView.findViewById(R.id.forum_name);
        timeTv =  itemView.findViewById(R.id.forum_time);
        watchNumble =  itemView.findViewById(R.id.forum_item_watcher);
        commentNumble =  itemView.findViewById(R.id.forum_item_comment);
        loveNumble =  itemView.findViewById(R.id.forum_item_agree);
        all=  itemView.findViewById(R.id.linear_all);
        contentTv =  itemView.findViewById(R.id.forum_item_content);*/
        photos=new ArrayList<>();
        photoInfos=new ArrayList<>();

    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

}
