package com.wta.NewCloudApp.jiuwei99986.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.model.ConsultData;
import com.wta.NewCloudApp.jiuwei99986.widget.ConstrainImage;
import com.wta.NewCloudApp.jiuwei99986.widget.RoundImageView;

import java.util.List;

/**
 * Created by 小小程序员 on 2017/9/21.
 */

public class ConsultAdapter extends BaseQuickAdapter<ConsultData.DataEntity,BaseViewHolder>{
    private Context context;
    public ConsultAdapter(@LayoutRes int layoutResId, @Nullable List<ConsultData.DataEntity> data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ConsultData.DataEntity item) {
        helper.setText(R.id.anchor_nick,item.getNick())
                .setText(R.id.anchor_title,item.getTitle())
                .addOnClickListener(R.id.share)
                .setText(R.id.consult_state,item.getLive_status().equals(Constants.ONE)?"直播中":"预告")
                .setText(R.id.consult_time,item.getStatime());
        RoundImageView roundImageView=helper.getView(R.id.anchor_head);
        ConstrainImage constrainImage=helper.getView(R.id.consult_image);
        Glide.with(context).load(item.getPhoto()).into(roundImageView);
        Glide.with(context).load(item.getLive_cover()).into(constrainImage);

    }
}
