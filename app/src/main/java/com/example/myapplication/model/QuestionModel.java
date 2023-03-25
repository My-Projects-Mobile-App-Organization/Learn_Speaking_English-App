package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionModel implements Serializable {
    int typeQues;
    String titleQues;
    ArrayList<ContentWordAnsMatchFrag> listContentQues;
    ArrayList<AudioWordAnsMatchFrag> listChoiceQues;
    ArrayList<Integer> listAnswer;
    boolean statusQuestion;

    public boolean isStatusQuestion() {
        return statusQuestion;
    }

    public void setStatusQuestion(boolean statusQuestion) {
        this.statusQuestion = statusQuestion;
    }

    public int getTypeQues() {
        return typeQues;
    }

    public void setTypeQues(int typeQues) {
        this.typeQues = typeQues;
    }

    public String getTitleQues() {
        return titleQues;
    }

    public void setTitleQues(String titleQues) {
        this.titleQues = titleQues;
    }

    public ArrayList<ContentWordAnsMatchFrag> getListContentQues() {
        return listContentQues;
    }

    public void setListContentQues(ArrayList<ContentWordAnsMatchFrag> listContentQues) {
        this.listContentQues = listContentQues;
    }

    public ArrayList<AudioWordAnsMatchFrag> getListChoiceQues() {
        return listChoiceQues;
    }

    public void setListChoiceQues(ArrayList<AudioWordAnsMatchFrag> listChoiceQues) {
        this.listChoiceQues = listChoiceQues;
    }

    public ArrayList<Integer> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(ArrayList<Integer> listAnswer) {
        this.listAnswer = listAnswer;
    }

    public QuestionModel(int type, String titleQues, ArrayList<ContentWordAnsMatchFrag> listContentQues, ArrayList<AudioWordAnsMatchFrag> listChoiceQues, ArrayList<Integer> listAnswer) {
        this.typeQues = type;
        this.titleQues = titleQues;
        this.listContentQues = listContentQues;
        this.listChoiceQues = listChoiceQues;
        this.listAnswer = listAnswer;
        this.statusQuestion = false;
    }
}
