package com.example.ginshinimpact_project2_cs310;

public class Comment {
    public int rating;
    public String commentId;
    public String userId;
    public String content;

    public Comment() { }

    public Comment(String commentId, String userId, String content, int rating) {
        this.rating = rating;
        this.commentId = commentId;
        this.userId = userId;
        this.content = content;
    }
}
