package com.wta.NewCloudApp.jiuwei99986.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.views.activity.RandomSayActivity;


/**
 * Created by mj
 * on 2016/10/28.
 */
public class PopupMenuUtil {
    ObjectAnimator anim,xAnim,scaleX,scaleY,objectAnimator;
    AnimatorSet animatorSet;
    private static final String TAG = "PopupMenuUtil";
    private Context context;
    private View rootVew;
    private PopupWindow popupWindow;
    ImageView add;
    private TextView left, center, right;
    private void _createView(final Context context) {
        rootVew = LayoutInflater.from(context).inflate(R.layout.popup_add, null);
        left = (TextView) rootVew.findViewById(R.id.add_left);
        right = (TextView) rootVew.findViewById(R.id.add_right);
        center = (TextView) rootVew.findViewById(R.id.add_center);
        add = (ImageView) rootVew.findViewById(R.id.add);
        add.setOnClickListener(new MViewClick(0,context));
        left.setOnClickListener(new MViewClick(1,context));
        right.setOnClickListener(new MViewClick(2,context));
        center.setOnClickListener(new MViewClick(3,context));
        rootVew.setOnClickListener(new MViewClick(4,context));
        popupWindow = new PopupWindow(rootVew,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //设置为失去焦点 方便监听返回键的监听
        popupWindow.setFocusable(false);
        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        //popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);

    }


    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }



    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {
        public int index;
        public Context context;
        public MViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            switch (index){
                case 0:
                    closePopupWindowAction(500);
                    break;
                case 1:
                    _close(5);
                    context.startActivity(new Intent(context, RandomSayActivity.class));
                    break;
                case 2:
                    _close(5);
                    Intent intent =new Intent(context, RandomSayActivity.class);
                    intent.putExtra(Constants.FLAG,Constants.FLAG);
                    context.startActivity(intent);
                    break;
                case 3:
                    Toast.makeText(context, "center", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    closePopupWindowAction(500);
                    break;
                case 5:
                    _show(context,rootVew);
                    break;
            }
        }
    }

    Toast toast = null;

    /**
     * 防止toast 多次被创建
     *
     * @param context context
     * @param str     str
     */
    private void showToast(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }





    /**
     * 弹起 popupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void _show(Context context, View parent) {
        this.context = context;
        _createView(context);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            openPopupWindowAction();
        }
    }

    /**
     * 关闭popupWindow
     */

    public void _close(int time) {
        if (popupWindow != null && popupWindow.isShowing()) {
            closePopupWindowAction(time);
        }
    }

    /**
     * @return popupWindow 是否显示了
     */
    public boolean _isShowing() {
        if (popupWindow == null) {
            return false;
        } else {
            return popupWindow.isShowing();
        }
    }

    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     mView
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void _closeAnimation(View view, int duration, int next) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }

    private void openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(add, "rotation", 0f, 135f);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        startAnimation(left, 300, new float[]{dip2px(context, 0), dip2px(context, -50), dip2px(context, -100),}, new float[]{0, dip2px(context, -40), dip2px(context, -80),});
        startAnimation(right, 400, new float[]{dip2px(context, 0), dip2px(context, -50), dip2px(context, -100),}, new float[]{0, dip2px(context, 40), dip2px(context, 80)});
        startAnimation(center, 500, new float[]{dip2px(context, 0), dip2px(context, -50), dip2px(context, -130),}, new float[]{0});
    }

    private void closePopupWindowAction(int time) {
        add.setClickable(false);
        add.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    add.setClickable(true);
                }
            }
        }, time);

        objectAnimator = ObjectAnimator.ofFloat(add, "rotation", 135f, 0f);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        closeAnimation(left, 300, new float[]{dip2px(context, -100), dip2px(context, -50), dip2px(context, 0),}, new float[]{dip2px(context, -80), dip2px(context, -40), dip2px(context, 0),});
        closeAnimation(right, 400, new float[]{dip2px(context, -100), dip2px(context, -50), dip2px(context, 0),}, new float[]{dip2px(context, 80), dip2px(context, 40), dip2px(context, 0)});
        closeAnimation(center, 500, new float[]{dip2px(context, -130), dip2px(context, -50), dip2px(context, 0),}, new float[]{0});


    }

    private void startAnimation(View view, int duration, float[] distance, float[] xDistance) {
         anim = ObjectAnimator.ofFloat(view, "translationY", distance);
         xAnim = ObjectAnimator.ofFloat(view, "translationX", xDistance);
         scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.2f, 0.5f, 1.0f);
         scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 0.5f, 1.0f);
         animatorSet = new AnimatorSet();
        animatorSet.playTogether(anim, xAnim, scaleX, scaleY);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }

    private void closeAnimation(View view, int duration, float[] distance, float[] xDistance) {
         anim = ObjectAnimator.ofFloat(view, "translationY", distance);
         xAnim = ObjectAnimator.ofFloat(view, "translationX", xDistance);
         scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.5f, 0.2f);
         scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.5f, 0.2f);
         animatorSet = new AnimatorSet();
        animatorSet.playTogether(anim, xAnim, scaleX, scaleY);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }
    public void stopAnims(){
        if (xAnim!=null){
            xAnim.cancel();
            xAnim.end();
            scaleY.cancel();
            scaleY.end();
            scaleX.cancel();
            scaleX.cancel();
            anim.cancel();
            anim.end();
            animatorSet.cancel();
            animatorSet.end();
        }
        if (objectAnimator!=null){
            objectAnimator.cancel();
            objectAnimator.end();
        }
    }
}
