package com.jcs.javacommunitysite.pages.askpage;

import java.util.List;

public class NewPostForm {
    private String title;
    private String content;
    private String ownerDid;
    private List<String> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwnerDid() {
        return ownerDid;
    }

    public void setOwnerDid(String ownerDid) {
        this.ownerDid = ownerDid;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

