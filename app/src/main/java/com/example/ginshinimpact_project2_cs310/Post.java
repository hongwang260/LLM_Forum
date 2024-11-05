package com.example.ginshinimpact_project2_cs310;
import java.util.Map;

public class Post {
    public String postId;
    public String title;
    public String llmKind;
    public String content;
    public String authorNotes;
    public Map<String, Comment> comments; // Map of comments, where each key is a comment ID

    public Post() { }

    public Post(String postId, String title, String llmKind, String content, String authorNotes) {
        this.postId = postId;
        this.title = title;
        this.llmKind = llmKind;
        this.content = content;
        this.authorNotes = authorNotes;
        this.comments = null; // Initialize with null or an empty map when no comments are added
    }

    public Post(String postId, String title, String llmKind, String content, String authorNotes, Map<String, Comment> comments) {
        this.postId = postId;
        this.title = title;
        this.llmKind = llmKind;
        this.content = content;
        this.authorNotes = authorNotes;
        this.comments = comments;
    }
}
