package com.example.onlinechatapp.models;

public class Message_Model {
    String msg,userid,time;

    public Message_Model(String msg,String userid){
        this.msg=msg;
        this.userid=userid;
        this.time=time;
    }

    public Message_Model(String msg,String userid,String time){
        this.msg=msg;
        this.userid=userid;
        this.time=time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
