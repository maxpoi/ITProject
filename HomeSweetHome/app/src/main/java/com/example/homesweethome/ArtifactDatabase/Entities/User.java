package com.example.homesweethome.ArtifactDatabase.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    private int email;

    private String userName;
    private String birthday;
    private String title;
    private String date;
    private String desc;
    private String coverImagePath;
}