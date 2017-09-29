package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wta.NewCloudApp.jiuwei99986.BuildConfig;
import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.Compress;
import com.wta.NewCloudApp.jiuwei99986.model.LiveRoomMessage;
import com.wta.NewCloudApp.jiuwei99986.presenter.ConsultHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.CommonUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.PictureUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.ProgressDialogUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.RxToast;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;
import com.wta.NewCloudApp.jiuwei99986.views.customs.EditDialog;
import com.wta.NewCloudApp.jiuwei99986.views.customs.EditPassDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 小小程序员 on 2017/9/20.
 */

public class ModifyLiveMessage extends BaseActivity {


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.tool_content)
    TextView toolContent;
    @BindView(R.id.consult_introduce)
    TextView consultIntroduce;
    @BindView(R.id.consult_cover)
    ImageView consultCover;
    @BindView(R.id.consult_pass)
    TextView consultPass;
    @BindView(R.id.cover_contain)
    FrameLayout frameLayout;
    @BindView(R.id.is_agree)
    TextView isAgree;
    LiveRoomMessage.DataEntity dataEntity;
    private ConsultHelp consultHelp;
    private Map<String, RequestBody> map = new HashMap<>();
    String cropImagePath, introduce;
    File image, tempFile;
    boolean isClip, isModifyIntroduce;
    ProgressDialog progressDialog;
    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.live_room_message;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        dataEntity = (LiveRoomMessage.DataEntity) getIntent().getSerializableExtra(Constants.FLAG);
        if (dataEntity != null) {
            consultIntroduce.setText(dataEntity.getLive_description());
            consultPass.setText(dataEntity.getLive_password());
            Glide.with(this).load(dataEntity.getLive_cover()).into(consultCover);
        }
        toolContent.setText(getString(R.string.consult_modify));
        consultHelp = new ConsultHelp();

        SharedPrefrenceManager.getInstance().setDisplayWidth(getWindowManager().getDefaultDisplay().getWidth());
    }

    @OnClick({R.id.agree, R.id.introduce, R.id.cover_contain, R.id.cover, R.id.pass, R.id.toolbar_back})
    public void onViewsClick(View view) {
        switch (view.getId()) {
            case R.id.agree:
                break;
            case R.id.introduce:
                modifyIntroduce();
                //CommonUtils.showSoftInput(this,editDialog.getEditText());
                break;
            case R.id.cover:
            case R.id.cover_contain:
                type = 2;
                modifyImage();
                break;
            case R.id.pass:
                modifyPassWord();
                break;
            case R.id.toolbar_back:
                finish();
                break;

        }
    }

    private void modifyPassWord() {
        final EditPassDialog passDialog = new EditPassDialog(this);
        passDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.equals(passDialog.getSure().getText().toString(), passDialog.getNewd().getText().toString())) {
                    RxToast.normal(getString(R.string.consult_pass_equal), true);
                    return;
                }
                map.clear();
                map.put(Constants.LIVE_ID, RequestBody.create(MediaType.parse("text/plain"), dataEntity != null ? dataEntity.getLiveid() : ""));
                map.put("live_password", RequestBody.create(MediaType.parse("text/plain"), passDialog.getSure().getText().toString()));
                consultHelp.modifyLiveRoom(map);
                isModifyIntroduce = false;
                passDialog.cancel();
            }
        });
        passDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDialog.cancel();
            }
        });
        passDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showSoftInput(ModifyLiveMessage.this, passDialog.getNewd());
            }
        }, 100);
    }

    //修改介绍
    private void modifyIntroduce() {
        final EditDialog editDialog = new EditDialog(this);
        editDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editDialog.getEditText().getText().toString())) {
                    RxToast.normal(getResources().getString(R.string.empty_content));
                    return;
                }
                map.clear();
                map.put(Constants.LIVE_ID, RequestBody.create(MediaType.parse("text/plain"), dataEntity != null ? dataEntity.getLiveid() : ""));
                map.put("live_description", RequestBody.create(MediaType.parse("text/plain"), editDialog.getEditText().getText().toString()));
                introduce = editDialog.getEditText().getText().toString();
                isModifyIntroduce = true;
                consultHelp.modifyLiveRoom(map);
                editDialog.cancel();
            }
        });
        editDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.cancel();
            }
        });
        editDialog.getEditText().setText(dataEntity != null ? dataEntity.getLive_description() : "");
        editDialog.show();
        editDialog.getEditText().requestFocus();
        //editDialog.getEditText().setFocusableInTouchMode(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showSoftInput(ModifyLiveMessage.this, editDialog.getEditText());
            }
        }, 100);
    }

    //修改直播间封面
    private void modifyImage() {
        //ce
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.live_room_message, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(ModifyLiveMessage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(ModifyLiveMessage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(ModifyLiveMessage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(ModifyLiveMessage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), Constants.REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        //创建拍照存储的图片文件
        tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(ModifyLiveMessage.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, Constants.REQUEST_CAPTURE);
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constants.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case Constants.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case Constants.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    isClip = true;
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    if (type == 2) {
                        consultCover.setImageBitmap(bitMap);
                        progressDialog = ProgressDialogUtils.showProgressDialog(ModifyLiveMessage.this, getString(R.string.dialog_modify));
                        compressPhoto();
                        //headImage1.setImageBitmap(bitMap);
                    } else {
                        //headImage2.setImageBitmap(bitMap);
                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }

    //压缩上传
    private void compressPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                image = PictureUtils.getImage(cropImagePath, true, Constants.SCALE_SIZE);
            }
        }).start();
    }

    //图片压缩后上传
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void isCompressed(Compress co) {
        publishImage();
    }

    private void publishImage() {
        map.clear();
        map.put(Constants.LIVE_ID, RequestBody.create(MediaType.parse("text/plain"), dataEntity != null ? dataEntity.getLiveid() : ""));
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), image);
        map.put("fileUpload[]\"; filename=\"" + image.getName() , fileBody);
        consultHelp.modifyLiveRoom(map);
        isModifyIntroduce = false;
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, Constants.REQUEST_CROP_PHOTO);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void getState(BaseData baseData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (baseData != null) {
            if (baseData.getFlag().equals(Constants.ONE)) {
                RxToast.normal(getString(R.string.modify_success), true);
                if (isModifyIntroduce){
                    consultIntroduce.setText(introduce);
                }
                if (image != null && image.exists()) {
                    image.delete();
                }
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (consultHelp != null) {
            consultHelp.onDestroy();
        }
        if (image != null && image.exists()) {
            image.delete();
        }
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }
}
