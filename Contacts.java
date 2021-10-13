package com.some.studychats;

import java.util.jar.Attributes;

public class Contacts {

    public String name,status,image,groupName,receiver,sender,messageID;
    private String time;
    private String date;

    private boolean isseen;;


    public Contacts(){

    }

    public Contacts(boolean isseen) {
        this.isseen = isseen;
    }

    public Contacts(String name, String status, String image, String groupName, String receiver, String sender, String messageID
    ,String time,String date) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.groupName = groupName;
        this.receiver = receiver;
        this.sender = sender;
        this.messageID = messageID;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public void setImage(String image) {
        this.image = image;


    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
