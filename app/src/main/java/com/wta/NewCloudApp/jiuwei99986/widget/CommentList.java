package com.wta.NewCloudApp.jiuwei99986.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.model.FindDetailData;
import com.wta.NewCloudApp.jiuwei99986.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/7/9.
 */
public class CommentList extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<FindDetailData.DataEntity.CommentEntity.ReplyEntity> mDatas;
    private LayoutInflater layoutInflater ;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<FindDetailData.DataEntity.CommentEntity.ReplyEntity> datas){
        if(datas == null ){
            datas = new ArrayList<FindDetailData.DataEntity.CommentEntity.ReplyEntity>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<FindDetailData.DataEntity.CommentEntity.ReplyEntity> getDatas(){
        return mDatas;
    }

    public CommentList(Context context) {
        super(context);
    }

    public CommentList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textView的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.praise_item_default));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.praise_item_selector_default));

        }finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged(){
        removeAllViews();
        if(mDatas == null || mDatas.size() == 0){
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<mDatas.size(); i++){
            final int index = i;
            View view = getView(index);
            if(view == null){
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position){
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);
        final FindDetailData.DataEntity.CommentEntity.ReplyEntity bean = mDatas.get(position);
        String name = bean.getMembername();
        String id = bean.getPid();
        String toReplyName = "";
        if (bean.getRemembername() != null) {
            toReplyName = bean.getRemembername();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, bean.getPid()));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getRemembername()));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getContent();
        builder.append(Utils.getInstace().getExpressionString(App.getContext(), contentBodyStr));
        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.appColor));
        subjectSpanText.setSpan(colorSpan,0,subjectSpanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        /*subjectSpanText.setSpan(new SpannableClickable(itemColor){
                                    @Override
                                    public void onClick(View widget) {
                                        //Toast.makeText(App.getContext(), textStr + " &id = " + id, Toast.LENGTH_SHORT).show();
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        return subjectSpanText;
    }

    public static interface OnItemClickListener{
        public void onItemClick(int position);
    }

    public static interface OnItemLongClickListener{
        public void onItemLongClick(int position);
    }


}
