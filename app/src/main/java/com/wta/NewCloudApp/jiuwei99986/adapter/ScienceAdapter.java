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
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.model.PhotoInfo;
import com.wta.NewCloudApp.jiuwei99986.model.ScienceData;
import com.wta.NewCloudApp.jiuwei99986.utils.Utils;
import com.wta.NewCloudApp.jiuwei99986.widget.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小小程序员 on 2017/8/29.
 */

public class ScienceAdapter extends BaseQuickAdapter<ScienceData.DataEntity, BaseViewHolder> {
    private Context context;
    public List<String> photos = new ArrayList<>();
    public List<PhotoInfo> photoInfos = new ArrayList<>();
    private boolean isInflate = false;
    private View subView;

    public ScienceAdapter(@LayoutRes int layoutResId, @Nullable List<ScienceData.DataEntity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        Utils.getInstace().initSort();
    }

    @Override
    protected void convert(BaseViewHolder helper, ScienceData.DataEntity item) {
        helper.setText(R.id.find_item_agree, "点赞·" + item.getUpvotes())
                .setText(R.id.find_item_comment, "评论·" + item.getComments())
                .setText(R.id.find_item_watcher, "浏览量·" + item.getHits())
                .setText(R.id.find_name, item.getMembername())
                .setText(R.id.find_send_time, item.getDateline())
                .setText(R.id.find_item_sort, Utils.getInstace().parseSort(item.getFid()))
                .addOnClickListener(R.id.find_content)
                .addOnClickListener(R.id.science_cover);

        ImageView head = helper.getView(R.id.headIv);
        Glide.with(context).load(item.getMemberphoto()).transform(new GlideCircleTransform(context)).into(head);
        helper.setVisible(R.id.science_cover, true);
        helper.setVisible(R.id.find_content, false);
        ImageView cover = helper.getView(R.id.science_cover);
        Glide.with(context).load(item.getPic_cover()).into(cover);


    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
