package com.example.myapplication.model;

public class StringChoiceConversation {
    String contentChoice;
    boolean isAnswer;

    public StringChoiceConversation(String contentChoice, boolean isAnswer) {
        this.contentChoice = contentChoice;
        this.isAnswer = isAnswer;
    }

    public String getContentChoice() {
        return contentChoice;
    }

    public void setContentChoice(String contentChoice) {
        this.contentChoice = contentChoice;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }
}
