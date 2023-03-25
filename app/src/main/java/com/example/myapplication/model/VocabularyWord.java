package com.example.myapplication.model;

import java.io.Serializable;

public class VocabularyWord implements Serializable {
    int vocabID;
    String titleVocab;
    String linkImgVocab;
    String pronounceVocab;
    String typeVocab;
    String exampleVocab;
    String translateExampleVocab;
    String audioVocab;

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    boolean statusVocab;
    boolean isFlipped;

    public String getLinkImgVocab() {
        return linkImgVocab;
    }

    public void setLinkImgVocab(String linkImgVocab) {
        this.linkImgVocab = linkImgVocab;
    }

    public VocabularyWord(int vocabID, String titleVocab, String linkImgVocab, String pronounceVocab,
                          String typeVocab, String exampleVocab, String translateExampleVocab, String audioVocab, boolean statusVocab) {
        this.vocabID = vocabID;
        this.titleVocab = titleVocab;
        this.linkImgVocab = linkImgVocab;
        this.pronounceVocab = pronounceVocab;
        this.typeVocab = typeVocab;
        this.exampleVocab = exampleVocab;
        this.translateExampleVocab = translateExampleVocab;
        this.audioVocab = audioVocab;
        this.statusVocab = statusVocab;
        this.isFlipped = false;
    }

    public int getVocabID() {
        return vocabID;
    }

    public void setVocabID(int vocabID) {
        this.vocabID = vocabID;
    }

    public String getTitleVocab() {
        return titleVocab;
    }

    public void setTitleVocab(String titleVocab) {
        this.titleVocab = titleVocab;
    }

    public String getPronounceVocab() {
        return pronounceVocab;
    }

    public void setPronounceVocab(String pronounceVocab) {
        this.pronounceVocab = pronounceVocab;
    }

    public String getTypeVocab() {
        return typeVocab;
    }

    public void setTypeVocab(String typeVocab) {
        this.typeVocab = typeVocab;
    }

    public String getExampleVocab() {
        return exampleVocab;
    }

    public void setExampleVocab(String exampleVocab) {
        this.exampleVocab = exampleVocab;
    }

    public String getTranslateExampleVocab() {
        return translateExampleVocab;
    }

    public void setTranslateExampleVocab(String translateExampleVocab) {
        this.translateExampleVocab = translateExampleVocab;
    }

    public String getAudioVocab() {
        return audioVocab;
    }

    public void setAudioVocab(String audioVocab) {
        this.audioVocab = audioVocab;
    }

    public boolean isStatusVocab() {
        return statusVocab;
    }

    public void setStatusVocab(boolean statusVocab) {
        this.statusVocab = statusVocab;
    }
}
