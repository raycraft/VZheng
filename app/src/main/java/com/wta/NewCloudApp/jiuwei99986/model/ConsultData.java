package com.wta.NewCloudApp.jiuwei99986.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小小程序员 on 2017/9/21.
 */

public class ConsultData extends BaseData implements Serializable{
    private List<DataEntity> data;


    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable{
        private String liveid;
        private String live_cover;
        private String statime;
        private String nick;
        private String live_password;


        public String getLive_password() {
            return live_password;
        }

        public void setLive_password(String live_password) {
            this.live_password = live_password;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        private String userid;
        private String live_status;

        public String getLive_status() {
            return live_status;
        }

        public void setLive_status(String live_status) {
            this.live_status = live_status;
        }

        public String getLiveid() {
            return liveid;
        }

        public void setLiveid(String liveid) {
            this.liveid = liveid;
        }

        public String getLive_cover() {
            return live_cover;
        }

        public void setLive_cover(String live_cover) {
            this.live_cover = live_cover;
        }

        public String getStatime() {
            return statime;
        }

        public void setStatime(String statime) {
            this.statime = statime;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String photo;
        private String title;

    }
}
