package com.wta.NewCloudApp.jiuwei99986.views.customs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class EditPassDialog extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;
    private ClearableEditText newd,sure;
    private TextView mTvTitle;

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }



    public EditText getNewd() {
        return newd;
    }

    public void setNewd(ClearableEditText newd) {
        this.newd = newd;
    }

    public EditText getSure() {
        return sure;
    }

    public void setSure(ClearableEditText sure) {
        this.sure = sure;
    }




    public TextView getTvSure() {
        return mTvSure;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }


    public TextView getTvCancel() {
        return mTvCancel;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext_pass, null);
        mTvTitle = (TextView) dialog_view.findViewById(R.id.title);
        mTvSure = (TextView) dialog_view.findViewById(R.id.btn_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.btn_cancel);
        newd = (ClearableEditText) dialog_view.findViewById(R.id.consult_new_pass);
        sure = (ClearableEditText) dialog_view.findViewById(R.id.consult_sure_pass);
        setContentView(dialog_view);
        Window window= getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width= (int) SharedPrefrenceManager.getInstance().getDisplayWidth();
        window.setAttributes(attributes);
    }

    public EditPassDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public EditPassDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public EditPassDialog(Context context) {
        super(context);
        initView();
    }

    public EditPassDialog(Activity context) {
        super(context);
        initView();
    }

    public EditPassDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
