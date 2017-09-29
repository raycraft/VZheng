package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.adapter.SmallPhotoAdapter;
import com.wta.NewCloudApp.jiuwei99986.adapter.TagAdapter;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.custom.FlowTagLayout;
import com.wta.NewCloudApp.jiuwei99986.listener.OnItemSelectListener;
import com.wta.NewCloudApp.jiuwei99986.listener.OnTagSelectListener;
import com.wta.NewCloudApp.jiuwei99986.model.BaseData;
import com.wta.NewCloudApp.jiuwei99986.model.Compress;
import com.wta.NewCloudApp.jiuwei99986.presenter.DataHelp;
import com.wta.NewCloudApp.jiuwei99986.utils.PictureUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.ProgressDialogUtils;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 小小程序员 on 2017/9/1.
 */

public class RandomSayActivity extends BaseActivity implements OnItemSelectListener {
    private SmallPhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    @BindView(R.id.flow_tag)
    FlowTagLayout flowTagLayout;
    @BindView(R.id.photo_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.find_editText_publish)
    EditText sendEditText;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.say_publish)
    TextView publish;
    @BindView(R.id.say_content)
    TextView title;
    @BindView(R.id.public_linear)
    LinearLayout ask;
    @BindView(R.id.ask_public)
    RadioButton publics;
    @BindView(R.id.ask_private)
    RadioButton privates;
    private TagAdapter<String> tagAdapter;
    private String fid;
    private DataHelp dataHelp;
    private Map<String, RequestBody> postMap = new HashMap<>();
    private List<File> listFiles = new ArrayList<>();
    private boolean isCanSend, isContentWrite, isAsk, isPrivate;
    private ProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.random_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        title.setText(R.string.say);
        if (getIntent().getStringExtra(Constants.FLAG) != null) {
            isAsk = true;
            ask.setVisibility(View.VISIBLE);
            title.setText(R.string.ask);
        }

        photoAdapter = new SmallPhotoAdapter(this, selectedPhotos, this);
        tagAdapter = new TagAdapter<>(this);
        dataHelp = new DataHelp(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        flowTagLayout.setAdapter(tagAdapter);
        flowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        isCanSend = true;
                        fid = getResources().getStringArray(R.array.sortNumber)[i];
                        /*Snackbar.make(parent, "位置 = :" + fid, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*/
                    }
                } else {
                    isCanSend = false;
                }
                if (isContentWrite && isCanSend) {
                    publish.setTextColor(getResources().getColor(R.color.appColor));
                } else {
                    publish.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        tagAdapter.onlyAddAll(Arrays.asList(getResources().getStringArray(R.array.sort)));
        sendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    isContentWrite = true;
                } else {
                    isContentWrite = false;
                }
                if (isContentWrite && isCanSend) {
                    publish.setTextColor(getResources().getColor(R.color.appColor));
                } else {
                    publish.setTextColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void publishPost() {
        postMap.clear();
        postMap.put(Constants.USERID, RequestBody.create(MediaType.parse("text/plain"), SharedPrefrenceManager.getInstance().getUserId()));
        postMap.put("info", RequestBody.create(MediaType.parse("text/plain"), sendEditText.getText().toString()));
        postMap.put(Constants.FID, RequestBody.create(MediaType.parse("text/plain"), fid));
        for (File file : listFiles) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image"), file);
            postMap.put("fileUpload[]\"; filename=\"" + file.getName() , fileBody);
        }
        if (isAsk) {
            postMap.put("private", RequestBody.create(MediaType.parse("text/plain"), isPrivate?"1":"0"));
            dataHelp.publishAsk(postMap);
        } else {
            dataHelp.publishPost(postMap);
        }
    }

    //压缩图片
    private void compressPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < selectedPhotos.size(); i++) {
                    if (i == selectedPhotos.size() - 1) {
                        listFiles.add(PictureUtils.getImage(selectedPhotos.get(i), true, 100));
                    } else {
                        listFiles.add(PictureUtils.getImage(selectedPhotos.get(i), false, 100));
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.say_publish, R.id.ask_private, R.id.ask_public,R.id.toolbar_back})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.say_publish:
                if (TextUtils.isEmpty(sendEditText.getText().toString().trim())) {
                    Toast.makeText(RandomSayActivity.this, getString(R.string.empty_content), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isCanSend) {
                    Toast.makeText(RandomSayActivity.this, getString(R.string.toast_sort), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedPhotos.size() > 0) {
                    progressDialog = ProgressDialogUtils.showProgressDialog(RandomSayActivity.this, getString(R.string.dialog_hint));
                    compressPhoto();
                } else {
                    publishPost();
                }
                break;
            case R.id.ask_private:
                isPrivate = true;
                publics.setChecked(false);
                privates.setChecked(true);
                break;
            case R.id.ask_public:
                isPrivate = false;
                publics.setChecked(true);
                privates.setChecked(false);
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    private void deleteFiles() {
        for (int i = 0; i < listFiles.size(); i++) {
            if (listFiles.get(i).exists()) {
                listFiles.get(i).delete();
            }
        }
    }

    //图片压缩后上传
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void isCompressed(Compress co) {
        publishPost();
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void followResult(BaseData data) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (data == null) {
            Toast.makeText(RandomSayActivity.this, getResources().getString(R.string.toast_post_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getFlag().equals("0")) {
            Toast.makeText(RandomSayActivity.this, getResources().getString(R.string.toast_post_error), Toast.LENGTH_SHORT).show();
        } else {
            sendEditText.setText("");
            postMap.clear();
            deleteFiles();
            listFiles.clear();
            selectedPhotos.clear();
            SharedPrefrenceManager.getInstance().setCanRefresh(true);
            SharedPrefrenceManager.getInstance().setAskCanRedlash(true);
            finish();
        }
    }

    @Override
    public void onTabSelect(int position) {
        selectedPhotos.remove(position);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPictureSelect(int position) {
        if (photoAdapter.getItemViewType(position) == SmallPhotoAdapter.TYPE_ADD) {
            PhotoPicker.builder()
                    .setPhotoCount(SmallPhotoAdapter.MAX)
                    .setShowCamera(true)
                    .setPreviewEnabled(false)
                    .setSelected(selectedPhotos)
                    .start(RandomSayActivity.this);
        } else {
            PhotoPreview.builder()
                    .setPhotos(selectedPhotos)
                    .setCurrentItem(position)
                    .start(RandomSayActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHelp.onDestroy();
    }
}
