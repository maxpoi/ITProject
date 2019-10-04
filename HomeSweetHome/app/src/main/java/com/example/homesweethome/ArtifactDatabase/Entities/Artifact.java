package com.example.homesweethome.ArtifactDatabase.Entities;

import androidx.annotation.NonNull;
import androidx.room.*;

import java.util.ArrayList;

@Entity(tableName = "Artifacts")
public class Artifact {

    @PrimaryKey
    @NonNull
    private int id;

    private String video;
    private String audio;
    private String title;
    private String date;
    private String desc;
    private String coverImagePath;

    @Ignore
    public Artifact() { initialize(); }

    @Ignore
    public Artifact(int id) {
        this.id = id;
        initialize();
    }

    public Artifact(int id, String video, String audio, String title, String date, String desc, String coverImagePath) {
        this.id = id;
        this.video = video;
        this.audio = audio;
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.coverImagePath = coverImagePath;
    }

    @Ignore
    public Artifact(String title, String date, String desc, String video, String audio) {
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.video = video;
        this.audio = audio;
    }

    @Ignore
    private void initialize() {
        this.title = null;
        this.date = null;
        this.desc = null;
        this.video = null;
        this.audio = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public String getAudio() { return audio; }
    public void setAudio(String audio) { this.audio = audio; }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImagePath() { return coverImagePath; }
    public void setCoverImagePath(String coverImagePath) { this.coverImagePath = coverImagePath; }

}
