package com.example.homesweethome.AppDataBase.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    private int email;

    private String userName;
    private String DOB;
    private String gender;
    private String desc;
    private String portraitImagePath;

    public User(int email, String userName, String DOB, String gender, String desc, String portraitImagePath) {
        this.email = email;
        this.userName = userName;
        this.DOB = DOB;
        this.gender = gender;
        this.desc = desc;
        this.portraitImagePath = portraitImagePath;
    }

    public int getEmail() { return email; }
    public void setEmail(int email) { this.email = email; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getDOB() { return DOB; }
    public void setDOB(String DOB) { this.DOB = DOB; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getPortraitImagePath() { return portraitImagePath; }
    public void setPortraitImagePath(String portraitImagePath) { this.portraitImagePath = portraitImagePath; }
}