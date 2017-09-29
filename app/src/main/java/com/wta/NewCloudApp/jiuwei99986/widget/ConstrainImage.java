package com.wta.NewCloudApp.jiuwei99986.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wta.NewCloudApp.jiuwei99986.R;

/**
 * Created by 小小程序员 on 2017/6/29.
 */

public class ConstrainImage extends ImageView {
    private float scale=1;
    public ConstrainImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConstrainImage);
        scale=typedArray.getFloat(R.styleable.ConstrainImage_scaleNumber,1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode== MeasureSpec.EXACTLY){
            int heightSize= (int) (widthSize*scale);
            setMeasuredDimension(widthSize,heightSize);
        }

    }
}
