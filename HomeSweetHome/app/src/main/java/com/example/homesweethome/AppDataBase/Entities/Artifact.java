package com.example.homesweethome.AppDataBase.Entities;

import androidx.annotation.Nullable;
import androidx.room.*;

import java.util.ArrayList;

@Entity(tableName = "Artifacts")
public class Artifact implements Comparable<Artifact>{

    @PrimaryKey
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

    public Artifact(int id, String video, String audio, String title, String date, String desc) {
        this.id = id;
        this.video = video;
        this.audio = audio;
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.coverImagePath = coverImagePath;
    }

    @Ignore
    public Artifact(Artifact artifact) {
        this.id = artifact.getId();
        this.video = artifact.getVideo();
        this.audio = artifact.getAudio();
        this.title = artifact.getTitle();
        this.date = artifact.getTitle();
        this.desc = artifact.getDesc();
        this.coverImagePath = artifact.getCoverImagePath();
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        if (! (obj instanceof Artifact))
            return false;

        Artifact artifact = (Artifact) obj;
        return artifact.id == this.id;
    }

    @Override
    public int compareTo(Artifact t) {
        return this.date.substring(0,4).compareTo(t.date.substring(0,4));
    }
}
