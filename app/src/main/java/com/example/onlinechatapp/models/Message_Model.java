package com.example.onlinechatapp.models;

public class Message_Model {
    String msg,userid,time,timestamp,imageUrl,lat,lon,number,disp_name;

    public Message_Model(String msg,String userid,String time,String timestamp,String imageUrl,String lat,String lon
    ,String number,String disp_name){
        this.msg=msg;
        this.userid=userid;
        this.time=time;
        this.timestamp=timestamp;
        this.imageUrl=imageUrl;
        this.lat=lat;
        this.lon=lon;
        this.number=number;
        this.disp_name=disp_name;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisp_name() {
        return disp_name;
    }

    public void setDisp_name(String disp_name) {
        this.disp_name = disp_name;
    }
}

