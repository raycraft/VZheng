package com.wta.NewCloudApp.jiuwei99986.views.customs;


import com.wta.NewCloudApp.jiuwei99986.model.MemberInfo;

import java.util.ArrayList;


/**
 * 成员列表回调
 */
public interface MembersDialogView   {

    void showMembersList(ArrayList<MemberInfo.DataEntity> data);

}
