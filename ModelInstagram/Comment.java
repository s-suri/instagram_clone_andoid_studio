package com.some.studychats.ModelInstagram;

import java.util.Comparator;

public class Comment {
    private String comment;
    private String publisher;
    private String commentid;
    private String sender;
    private String receiver;
    private String senderImage;
    private String receiverImage;
    private String senderPdf;
    private String receiverPdf;
    private String time;
    private String date;
    private String message;
    private boolean isseen;
    private String type;
    private String messageID;
    private String uid;
    private String groupName;
    private String name;
    private String seenMessage;
    private String bio;
    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private String senderId;
    private String title;
    private String url;
    private String lastSendMessage;
    private String preMessage;


    public Comment(String comment, String publisher, String commentid, String sender, String receiver, String senderImage, String receiverImage, String senderPdf, String receiverPdf, String time, String date, String message, boolean isseen, String type, String messageID, String uid, String groupName, String name
    ,String seenMessage,String bio,String id, String username, String fullname, String imageurl,String senderId,String title,String url,String lastSendMessage
    ,String preMessage) {
        this.comment = comment;
        this.publisher = publisher;
        this.commentid = commentid;
        this.sender = sender;
        this.receiver = receiver;
        this.senderImage = senderImage;
        this.receiverImage = receiverImage;
        this.senderPdf = senderPdf;
        this.receiverPdf = receiverPdf;
        this.time = time;
        this.date = date;
        this.message = message;
        this.isseen = isseen;
        this.type = type;
        this.messageID = messageID;
        this.uid = uid;
        this.groupName = groupName;
        this.name = name;
        this.seenMessage = seenMessage;
        this.bio = bio;
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.senderId = senderId;
        this.title = title;
        this.url = url;
        this.lastSendMessage = lastSendMessage;
        this.preMessage = preMessage;
    }

    public Comment(String comment, String publisher, String commentid) {
        this.comment = comment;
        this.publisher = publisher;
        this.commentid = commentid;
    }

    public Comment() {
    }

    public static final Comparator<Comment> BY_NAME_ALPHABETICAL= new Comparator<Comment>() {
        @Override
        public int compare(Comment o1, Comment o2) {
            return o1.date.compareTo(o2.date);
        }
    };

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getSenderPdf() {
        return senderPdf;
    }

    public void setSenderPdf(String senderPdf) {
        this.senderPdf = senderPdf;
    }

    public String getReceiverPdf() {
        return receiverPdf;
    }

    public void setReceiverPdf(String receiverPdf) {
        this.receiverPdf = receiverPdf;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeenMessage() {
        return seenMessage;
    }

    public void setSeenMessage(String seenMessage) {
        this.seenMessage = seenMessage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastSendMessage() {
        return lastSendMessage;
    }

    public void setLastSendMessage(String lastSendMessage) {
        this.lastSendMessage = lastSendMessage;
    }

    public String getPreMessage() {
        return preMessage;
    }

    public void setPreMessage(String preMessage) {
        this.preMessage = preMessage;
    }

}
