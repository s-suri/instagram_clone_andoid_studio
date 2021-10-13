package com.some.studychats.Model;

import com.some.studychats.ModelInstagram.Comment;

import java.util.Comparator;

public class User {

    private String id;
    private String username;
    private String imageurl;
    private String status;
    private String search;
    private String groupName;


    public User(String id, String username, String imageurl, String status, String search,String groupName) {
        this.id = id;
        this.username = username;
        this.imageurl = imageurl;
        this.status = status;
        this.search = search;
        this.groupName = groupName;

    }

    public User() {

    }

    public static final Comparator<User> BY_NAME_ALPHABETICAL= new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return o1.username.compareTo(o2.username);
        }
    };

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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
