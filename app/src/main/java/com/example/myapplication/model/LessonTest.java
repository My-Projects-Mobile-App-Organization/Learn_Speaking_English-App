package com.example.myapplication.model;

import java.util.List;

public class LessonTest {
    private String lessonID;
    private String topicLesson;
    private String titleLesson;
    private int numberVocab;
    private List<VocabularyWord> listVocab;

    public String getLessonID() {
        return lessonID;
    }

    public void setLessonID(String lessonID) {
        this.lessonID = lessonID;
    }

    public String getTopicLesson() {
        return topicLesson;
    }

    public void setTopicLesson(String topicLesson) {
        this.topicLesson = topicLesson;
    }

    public String getTitleLesson() {
        return titleLesson;
    }

    public void setTitleLesson(String titleLesson) {
        this.titleLesson = titleLesson;
    }

    public int getNumberVocab() {
        return numberVocab;
    }

    public void setNumberVocab(int numberVocab) {
        this.numberVocab = numberVocab;
    }

    public List<VocabularyWord> getListVocab() {
        return listVocab;
    }

    public void setListVocab(List<VocabularyWord> listVocab) {
        this.listVocab = listVocab;
    }

    public LessonTest(String lessonID, String topicLesson, String titleLesson, int numberVocab, List<VocabularyWord> listVocab) {
        this.lessonID = lessonID;
        this.topicLesson = topicLesson;
        this.titleLesson = titleLesson;
        this.numberVocab = numberVocab;
        this.listVocab = listVocab;
    }
}
