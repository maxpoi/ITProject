package com.example.homesweethome.AppDataBase.Entities;

import androidx.room.Entity;
import androidx.room.Fts4;

@Entity
@Fts4(contentEntity = Artifact.class)
public class ArtifactFts {
    private String title;
    private String date;
    private String desc;

    public ArtifactFts(String title, String date, String desc) {
        this.title = title;
        this.date = date;
        this.desc = desc;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getDesc() { return desc; }
}
