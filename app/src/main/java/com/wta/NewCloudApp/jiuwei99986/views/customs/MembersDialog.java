package com.wta.NewCloudApp.jiuwei99986.views.customs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.ListView;

import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.listener.LiveView;
import com.wta.NewCloudApp.jiuwei99986.model.MemberInfo;
import com.wta.NewCloudApp.jiuwei99986.presenter.LiveHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 成员列表
 */
public class MembersDialog extends Dialog implements MembersDialogView {
    private Context mContext;
    //private GetMemberListHelper mGetMemberListHelper;
    private ListView mMemberList;
    private MembersAdapter mMembersAdapter;
    private ArrayList<MemberInfo.DataEntity> data = new ArrayList<MemberInfo.DataEntity>();
    private String liveid;
    private LiveHelp liveHelp;
    private Map<String,String> map=new HashMap<>();
    public MembersDialog(Context context, int theme, LiveView view, String liveid) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.members_layout);
        mMemberList = (ListView) findViewById(R.id.member_list);
        mMembersAdapter = new MembersAdapter(mContext, R.layout.members_item_layout, data, view, this);
        mMemberList.setAdapter(mMembersAdapter);
        Window window = getWindow();
        window.setGravity(Gravity.TOP);
        this.liveid=liveid;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onStart() {
        //获取成员信息
       /* mGetMemberListHelper = new GetMemberListHelper(mContext, this,liveid);
        mGetMemberListHelper.getMemberList();*/
        liveHelp=new LiveHelp(this);
        map.clear();
        map.put(Constants.LIVE_ID,liveid);
        liveHelp.audienceApply(map);
        super.onStart();
    }

    @Override
    protected void onStop() {
        //mGetMemberListHelper.onDestory();
        super.onStop();
    }

    /**
     * 通过Helper获得数据
     *
     * @param data
     */
    @Override
    public void showMembersList(ArrayList<MemberInfo.DataEntity> data) {
        if (data == null) return;
        mMembersAdapter.clear();
        for (int i = 0; i < data.size(); i++) {
            mMembersAdapter.insert(data.get(i), i);
        }
        mMembersAdapter.notifyDataSetChanged();
    }


}
