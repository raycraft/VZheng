package com.wta.NewCloudApp.jiuwei99986.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小小程序员 on 2017/9/20.
 */

public class LiveRoomMessage implements Serializable{
    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    private List<DataEntity> data;
    public static class DataEntity implements Serializable{
        private String liveid;
        private String title;
        private String live_cover;
        private String live_description;
        private String live_password;

        public String getLive_password() {
            return live_password;
        }

        public void setLive_password(String live_password) {
            this.live_password = live_password;
        }

        private String seized;

        public String getLiveid() {
            return liveid;
        }

        public void setLiveid(String liveid) {
            this.liveid = liveid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLive_cover() {
            return live_cover;
        }

        public void setLive_cover(String live_cover) {
            this.live_cover = live_cover;
        }

        public String getLive_description() {
            return live_description;
        }

        public void setLive_description(String live_description) {
            this.live_description = live_description;
        }

        public String getSeized() {
            return seized;
        }

        public void setSeized(String seized) {
            this.seized = seized;
        }
    }

}
