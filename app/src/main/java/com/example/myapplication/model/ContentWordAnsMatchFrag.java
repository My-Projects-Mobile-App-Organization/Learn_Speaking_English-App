package com.example.myapplication.model;

public class ContentWordAnsMatchFrag {
    String content;
    int idContent;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIdContent() {
        return idContent;
    }

    public void setIdContent(int idContent) {
        this.idContent = idContent;
    }

    public ContentWordAnsMatchFrag(String content, int idContent) {
        this.content = content;
        this.idContent = idContent;
    }
}
