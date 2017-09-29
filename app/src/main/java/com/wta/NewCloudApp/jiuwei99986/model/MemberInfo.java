package com.wta.NewCloudApp.jiuwei99986.model;

import java.util.ArrayList;

public class MemberInfo extends BaseData{
    private ArrayList<DataEntity> data;

    public ArrayList<DataEntity> getData() {
        return data;
    }

    public void setData(ArrayList<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String memberphone = "";
        private String membernick = "";
        private String avatar = "";
        private boolean isOnVideoChat = false;

        public String getUserId() {
            return memberphone;
        }

        public void setUserId(String userId) {
            this.memberphone = userId;
        }

        public String getUserName() {
            return membernick;
        }

        public void setUserName(String userName) {
            this.membernick = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public boolean isOnVideoChat() {
            return isOnVideoChat;
        }

        public void setIsOnVideoChat(boolean isOnVideoChat) {
            this.isOnVideoChat = isOnVideoChat;
        }

    }


}