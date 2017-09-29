package com.wta.NewCloudApp.jiuwei99986.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wta.NewCloudApp.jiuwei99986.R;

/**
 * Created by Rjm on 2016/4/10.
 */
public class DivItemDecoration extends RecyclerView.ItemDecoration {
    private int divHeight;
    private boolean hasHead;
    private Paint dividePaint;
    public DivItemDecoration(Context context, boolean hasHead){
        this.hasHead = hasHead;
        dividePaint = new Paint();
        dividePaint.setColor(context.getResources().getColor(R.color.f6));
        divHeight= (int) context.getResources().getDimension(R.dimen.h6);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if(hasHead && position == 0){
            return;
        }
        outRect.bottom = divHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount= parent.getChildCount();
        int left= parent.getPaddingLeft();
        int right=parent.getWidth()- parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            c.drawRect(left,view.getBottom(),right,view.getBottom()+divHeight,dividePaint);
        }

    }
}