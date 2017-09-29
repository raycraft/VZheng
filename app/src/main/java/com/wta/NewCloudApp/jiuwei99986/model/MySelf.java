package com.wta.NewCloudApp.jiuwei99986.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小小程序员 on 2017/1/17.
 */

public class MySelf implements Serializable {

    /**
     * flag : 1
     * data : {"userid":"2012","balancevcoin":"8743","photo":"http://img.weimeijieyuan.com/uploads/imageapp/member_noavatar_man.png","nick":"小郑医生","surplus":"994","beforestart":"10","slottime":"240"}
     */
    private DataEntity data;


    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable {
        /**
         * userid : 2012
         * balancevcoin : 8743
         * photo : http://img.weimeijieyuan.com/uploads/imageapp/member_noavatar_man.png
         * nick : 小郑医生
         * surplus : 994
         * beforestart : 10
         * slottime : 240
         */
        private String storage;

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        private String userid;
        private String balancevcoin;
        private String photo;
        private String nick;
        private String surplus;
        private String beforestart;
        private String slottime;
        private String online_number;
        private List<String> smallphotos;

        private String flag;
        private String sign;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }


        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
        public String getOnline_number() {
            return online_number;
        }

        public void setOnline_number(String online_number) {
            this.online_number = online_number;
        }

        public List<String> getSmallphotos() {
            return smallphotos;
        }

        public void setSmallphotos(List<String> smallphotos) {
            this.smallphotos = smallphotos;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getBalancevcoin() {
            return balancevcoin;
        }

        public void setBalancevcoin(String balancevcoin) {
            this.balancevcoin = balancevcoin;
        }

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

        public String getSurplus() {
            return surplus;
        }

        public void setSurplus(String surplus) {
            this.surplus = surplus;
        }

        public String getBeforestart() {
            return beforestart;
        }

        public void setBeforestart(String beforestart) {
            this.beforestart = beforestart;
        }

        public String getSlottime() {
            return slottime;
        }

        public void setSlottime(String slottime) {
            this.slottime = slottime;
        }
    }
}
