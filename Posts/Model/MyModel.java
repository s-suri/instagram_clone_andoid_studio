package com.some.studychats.Posts.Model;

public class MyModel {
    private String image_url;
    private String video_url;
    private String name;
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String type;
    private String sender;
    private String thumbnail;



    public MyModel(String image_url, String video_url, String name, String postid, String postimage, String description, String publisher, String type, String sender, String thumbnail) {
        this.image_url = image_url;
        this.video_url = video_url;
        this.name = name;
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.type = type;
        this.sender = sender;
        this.thumbnail = thumbnail;
    }

    public MyModel(){

    }

    public MyModel(String image_url, String name) {
        this.image_url = image_url;
        this.name = name;
    }

    public MyModel(String name) {
        this.image_url=null;
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getName() {
        return name;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
