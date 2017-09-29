package com.wta.NewCloudApp.jiuwei99986.model;

import java.io.Serializable;

/**
 * Created by 小小程序员 on 2017/9/22.
 */

public class SigData extends BaseData implements Serializable{
    private DataEntity data;
    private String chat;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable{
        public String sig;
        public String counselor_phonenumber;

        public String getAnchorid() {
            return counselor_phonenumber;
        }

        public void setAnchorid(String anchorid) {
            this.counselor_phonenumber = anchorid;
        }

        public String nick;
        public String photo;
        public String phonenumber;
        public String counselor_nick;
        public String counselor_photo;

        public String getCounselor_nick() {
            return counselor_nick;
        }

        public void setCounselor_nick(String counselor_nick) {
            this.counselor_nick = counselor_nick;
        }

        public String getCounselor_photo() {
            return counselor_photo;
        }

        public void setCounselor_photo(String counselor_photo) {
            this.counselor_photo = counselor_photo;
        }

        public String getSig() {
            return sig;
        }

        public void setSig(String sig) {
            this.sig = sig;
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

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }
    }
}
