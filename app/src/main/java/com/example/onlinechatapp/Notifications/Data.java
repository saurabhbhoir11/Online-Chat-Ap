package com.example.onlinechatapp.Notifications;

public class Data {
    private String username;
    private String icon;
    private String body;
    private String time;
    private String title;

    public Data(String username,String icon,String body,String time, String title){
        this.username=username;
        this.icon=icon;
        this.body=body;
        this.time=time;
        this.title=title;
    }

    public Data(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
