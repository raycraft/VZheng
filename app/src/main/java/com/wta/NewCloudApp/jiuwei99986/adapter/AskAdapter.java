package com.wta.NewCloudApp.jiuwei99986.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.model.AskData;
import com.wta.NewCloudApp.jiuwei99986.model.PhotoInfo;
import com.wta.NewCloudApp.jiuwei99986.utils.Utils;
import com.wta.NewCloudApp.jiuwei99986.widget.ExpandTextView;
import com.wta.NewCloudApp.jiuwei99986.widget.GlideCircleTransform;
import com.wta.NewCloudApp.jiuwei99986.widget.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小小程序员 on 2017/8/29.
 */

public class AskAdapter extends BaseQuickAdapter<AskData.DataEntity, BaseViewHolder> {
    private Context context;
    //
    public List<String> photos = new ArrayList<>();
    public List<PhotoInfo> photoInfos = new ArrayList<>();


    public AskAdapter(@LayoutRes int layoutResId, @Nullable List<AskData.DataEntity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        Utils.getInstace().initSort();
    }

    @Override
    protected void convert(BaseViewHolder helper, AskData.DataEntity item) {
        helper.setText(R.id.find_item_agree, "点赞·" + item.getUpvotes())
                .setText(R.id.find_item_comment, "评论·" + item.getComments())
                .setText(R.id.find_item_watcher, "浏览量·" + item.getHits())
                .setText(R.id.find_name, item.getMembername())
                .setText(R.id.find_send_time, item.getDateline())
                .setText(R.id.find_item_sort, Utils.getInstace().parseSort(item.getFid()))
                .addOnClickListener(R.id.find_content);

        ImageView head = helper.getView(R.id.headIv);
        Glide.with(context).load(item.getMemberphoto()).transform(new GlideCircleTransform(context)).error(R.mipmap.ic_launcher).into(head);
            ExpandTextView expandTextView = helper.getView(R.id.find_item_content);
            expandTextView.setText(Utils.getInstace().getExpressionString(App.getContext(),item.getContent()));
            MultiImageView multiImageView = helper.getView(R.id.multiImagView);
            photos = item.getPics();
            if (photos != null && photos.size() > 0) {
                photoInfos.clear();
                for (String url : photos) {
                    PhotoInfo photoInfo1 = new PhotoInfo();
                    photoInfo1.url = url;
                    photoInfo1.w = 240;
                    photoInfo1.h = 282;
                    photoInfos.add(photoInfo1);
                }
                multiImageView.setVisibility(View.VISIBLE);
                multiImageView.setList(photoInfos);
            } else {
                multiImageView.setVisibility(View.GONE);
            }


    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
