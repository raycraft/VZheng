package com.wta.NewCloudApp.jiuwei99986.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.widget.MultiImageView;


/**
 * Created by suneee on 2016/8/16.
 */
public class ForumImageViewHolder extends FindViewHolder {
    /** 图片*/
    public MultiImageView multiImageView;

    public ForumImageViewHolder(View itemView){
        super(itemView);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        Log.i("TAG", "initSubView: ");
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
        if(multiImageView != null){
            this.multiImageView = multiImageView;
        }
    }
}
