package com.example.onlinechatapp.models;

public class Message_Model {
    String msg,userid,time,timestamp,imageUrl,lat,lon;

    public Message_Model(String msg,String userid,String time,String timestamp,String imageUrl,String lat,String lon){
        this.msg=msg;
        this.userid=userid;
        this.time=time;
        this.timestamp=timestamp;
        this.imageUrl=imageUrl;
        this.lat=lat;
        this.lon=lon;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}

