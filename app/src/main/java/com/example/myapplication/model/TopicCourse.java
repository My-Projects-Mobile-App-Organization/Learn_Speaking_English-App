package com.example.myapplication.model;

public class TopicCourse {
    String topicID;
    String topicName;
    String topicImage;
    String UrlCat;
    int numberOfLessonTest;

    public int getNumberOfLessonTest() {
        return numberOfLessonTest;
    }

    public void setNumberOfLessonTest(int numberOfLessonTest) {
        this.numberOfLessonTest = numberOfLessonTest;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicImage() {
        return topicImage;
    }

    public void setTopicImage(String topicImage) {
        this.topicImage = topicImage;
    }

    public String getUrlCat() {
        return UrlCat;
    }

    public void setUrlCat(String urlCat) {
        UrlCat = urlCat;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public TopicCourse(String topicID, String topicName, String topicImage, String urlCat) {
        this.topicID = topicID;
        this.topicName = topicName;
        this.topicImage = topicImage;
        UrlCat = urlCat;
        this.numberOfLessonTest = 3;
    }
}
