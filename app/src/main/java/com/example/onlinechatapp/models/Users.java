package com.example.onlinechatapp.models;

public class Users {
    String profilepic, username, lastmsg, mail, userid,pass, tagline, time, status, follow,name;



    public Users(String profilepic, String username, String lastmsg, String mail, String userid, String pass, String tagline, String time, String status, String follow) {

        this.profilepic = profilepic;
        this.username = username;
        this.lastmsg = lastmsg;
        this.mail = mail;
        this.userid = userid;
        this.pass = pass;
        this.tagline = tagline;
        this.time = time;
        this.status = status;
        this.follow = follow;
    }
    public Users(){}

    public Users(String name,String mail, String pass){

            this.mail = mail;
            this.pass = pass;
            this.name=name;
    }


    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
