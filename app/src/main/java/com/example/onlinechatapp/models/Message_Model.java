package com.example.onlinechatapp.models;

public class Message_Model {
    String msg,userid,time,timestamp,imageUrl;

    public Message_Model(String msg,String userid,String time,String timestamp,String imageUrl){
        this.msg=msg;
        this.userid=userid;
        this.time=time;
        this.timestamp=timestamp;
        this.imageUrl=imageUrl;
    }

    public Message_Model(String msg,String userid){
        this.msg=msg;
        this.userid=userid;
    }

    public Message_Model(){}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

