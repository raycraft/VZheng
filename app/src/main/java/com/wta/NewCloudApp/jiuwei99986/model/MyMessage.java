package com.wta.NewCloudApp.jiuwei99986.model;

import java.io.Serializable;

/**
 * Created by 小小程序员 on 2017/9/18.
 */

public class MyMessage extends BaseData implements Serializable{
    private MyMessage.DataEntity data;

    public MyMessage.DataEntity getData() {
        return data;
    }

    public void setData(MyMessage.DataEntity data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable{
        private String photo;
        private String nick;
        private String findnums;
        private String asknums;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getFindnums() {
            return findnums;
        }

        public void setFindnums(String findnums) {
            this.findnums = findnums;
        }

        public String getAsknums() {
            return asknums;
        }

        public void setAsknums(String asknums) {
            this.asknums = asknums;
        }
    }
}
