package com.example.myapplication.model;

public class AudioWordAnsMatchFrag {
    String audio;
    int idAudio;

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getIdAudio() {
        return idAudio;
    }

    public void setIdAudio(int idAudio) {
        this.idAudio = idAudio;
    }

    public AudioWordAnsMatchFrag(String audio, int idAudio) {
        this.audio = audio;
        this.idAudio = idAudio;
    }
}
