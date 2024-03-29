package com.progmobile.meetchup.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;


import java.util.Date;
import java.util.List;


public class Post extends Model {

    public static final String POST_COL = "posts";

    private String userID;
    private User user;
    private Date date;
    private String description;
    private String docURL;
    private String docMimeType;
    private List<Comment> commentsList;

    public Post(String id, User user, Date date, String description, String docURL, String docMimeType) {
        super(id, date);
        this.user = user;
        this.date = date;
        this.description = description;
        this.docURL = docURL;
        this.docMimeType = docMimeType;
        this.commentsList = null;
    }

    public Post() {
        super();
    }


    public String getUserID() {
        return userID;
    }

    @Exclude
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
        this.userID = user.getId();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocURL() {
        return docURL;
    }

    public void setDocURL(String docURL) {
        this.docURL = docURL;
    }

    public String getDocMimeType() {
        return docMimeType;
    }

    public void setDocMimeType(String docMimeType) {
        this.docMimeType = docMimeType;
    }

    public List<Comment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }
}
