package com.example.myapplication.model;

public class StringConversationModel {
    String content;
    boolean isStatus;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        isStatus = status;
    }

    public StringConversationModel(String content, boolean isStatus) {
        this.content = content;
        this.isStatus = isStatus;
    }

    @Override
    public String toString() {
        return "StringConversationModel{" +
                "content='" + content + '\'' +
                ", isStatus=" + isStatus +
                '}';
    }
}
