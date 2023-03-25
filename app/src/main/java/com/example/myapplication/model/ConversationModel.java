package com.example.myapplication.model;

public class ConversationModel {
    int imgCharacterConversation;
    String contentConversation;
    String contentConversationToSpeak;
    boolean statusConversation;

    public boolean isStatusConversation() {
        return statusConversation;
    }

    public void setStatusConversation(boolean statusConversation) {
        this.statusConversation = statusConversation;
    }

    public String getContentConversationToSpeak() {
        return contentConversationToSpeak;
    }

    public void setContentConversationToSpeak(String contentConversationToSpeak) {
        this.contentConversationToSpeak = contentConversationToSpeak;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public ConversationModel(String contentConversation, int conversationType) {
        this.contentConversation = contentConversation;
        this.conversationType = conversationType;
        this.contentConversationToSpeak = contentConversation;
        if(conversationType==1){
            this.statusConversation = true;
        }else {
            this.statusConversation = false;
        }
    }

    int conversationType;

    public int getImgCharacterConversation() {
        return imgCharacterConversation;
    }

    public void setImgCharacterConversation(int imgCharacterConversation) {
        this.imgCharacterConversation = imgCharacterConversation;
    }

    public String getContentConversation() {
        return contentConversation;
    }

    public void setContentConversation(String contentConversation) {
        this.contentConversation = contentConversation;
    }

}
