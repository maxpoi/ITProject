package com.example.homesweethome.AppDataBase.Entities;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.room.*;

@Entity(tableName = "Images", primaryKeys = {"id", "artifactId"})  // indices = {@Index(value = {"id", "artifactId"}, unique = true)}
public class Image {

    private int id;
    private int artifactId;

    private String lowResImagePath;
    private String mediumResImagePath;
    private String highResImagePath;

    public Image(int id, int artifactId, String lowResImagePath, String mediumResImagePath, String highResImagePath) {
        this.id = id;
        this.artifactId = artifactId;

        initialize();
        this.lowResImagePath = lowResImagePath;
        this.mediumResImagePath = mediumResImagePath;
        this.highResImagePath = highResImagePath;
    }

    @Ignore
    private Bitmap lowImageBitmap;
    @Ignore
    private Bitmap mediumImageBitmap;
    @Ignore
    private Bitmap highImageBitmap;

    @Ignore
    private String originalPath;

    @Ignore
    public Image(String lowResImagePath, String mediumResImagePath, String highResImagePath) {
        this.lowResImagePath = lowResImagePath;
        this.mediumResImagePath = mediumResImagePath;
        this.highResImagePath = highResImagePath;
    }

    @Ignore
    public Image() { initialize(); }

    @Ignore
    public Image(int id) {
        this.id = id;
        initialize();
    }

    @Ignore
    private void initialize() {
        this.lowImageBitmap = null;
        this.mediumImageBitmap = null;
        this.highImageBitmap = null;
        this.lowResImagePath = null;
        this.mediumResImagePath = null;
        this.highResImagePath = null;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return this.id; }

    public int getArtifactId() { return artifactId; }
    public void setArtifactId(int artifactId) { this.artifactId = artifactId; }

    public void setLowResImagePath(String lowResImagePath) { this.lowResImagePath = lowResImagePath; }
    public void setMediumResImagePath(String mediumResImagePath) { this.mediumResImagePath = mediumResImagePath; }
    public void setHighResImagePath(String highResImagePath) { this.highResImagePath = highResImagePath; }

    public String getLowResImagePath() { return lowResImagePath; }
    public String getMediumResImagePath() { return mediumResImagePath; }
    public String getHighResImagePath() { return highResImagePath; }

    public Bitmap getLowImageBitmap() { return lowImageBitmap; }
    public Bitmap getMediumImageBitmap() { return mediumImageBitmap; }
    public Bitmap getHighImageBitmap() { return highImageBitmap; }

    public void setLowImageBitmap(Bitmap lowImageBitmap) { this.lowImageBitmap = lowImageBitmap; }
    public void setMediumImageBitmap(Bitmap mediumImageBitmap) { this.mediumImageBitmap = mediumImageBitmap; }
    public void setHighImageBitmap(Bitmap highImageBitmap) { this.highImageBitmap = highImageBitmap; }

    public void setOriginalPath(String path) { originalPath = path; }
    public String getOriginalPath() { return  originalPath; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        if (! (obj instanceof Image))
            return false;

        Image image = (Image) obj;
        return image.id == this.id && image.artifactId == this.artifactId;
    }
}
