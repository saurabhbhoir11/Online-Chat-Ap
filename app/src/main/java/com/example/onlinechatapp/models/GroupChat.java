package com.example.onlinechatapp.models;

public class GroupChat {
    String msg,sender,time,ImageUrl,VideoUrl,WebUrl, Stickers,audioUrl,timestamp;


    public GroupChat(String sender, String msg, String time) {
        this.sender = sender;
        this.msg = msg;
        this.time = time;
    }

    public GroupChat(String sender, String msg) {
        this.sender = sender;
        this.msg = msg;
    }

    public GroupChat(){}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp =timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getWebUrl() {
        return WebUrl;
    }

    public void setWebUrl(String webUrl) {
        WebUrl = webUrl;
    }

    public String getStickers() {
        return Stickers;
    }

    public void setStickers(String stickers) {
        Stickers = stickers;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
