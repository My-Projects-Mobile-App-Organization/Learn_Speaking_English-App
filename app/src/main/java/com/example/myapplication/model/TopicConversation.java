package com.example.myapplication.model;

public class TopicConversation {
    String linkTopicConversation;
    String contentTopicConversation;
    int imgTopicConversation;

    public String getLinkTopicConversation() {
        return linkTopicConversation;
    }

    public void setLinkTopicConversation(String linkTopicConversation) {
        this.linkTopicConversation = linkTopicConversation;
    }

    public String getContentTopicConversation() {
        return contentTopicConversation;
    }

    public void setContentTopicConversation(String contentTopicConversation) {
        this.contentTopicConversation = contentTopicConversation;
    }

    public int getImgTopicConversation() {
        return imgTopicConversation;
    }

    public void setImgTopicConversation(int imgTopicConversation) {
        this.imgTopicConversation = imgTopicConversation;
    }

    public TopicConversation(String linkTopicConversation, String contentTopicConversation, int imgTopicConversation) {
        this.linkTopicConversation = linkTopicConversation;
        this.contentTopicConversation = contentTopicConversation;
        this.imgTopicConversation = imgTopicConversation;
    }
}
