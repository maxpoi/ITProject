package com.example.homesweethome.HelperClasses;

import android.app.Application;

import com.example.homesweethome.ArtifactDatabase.ArtifactDatabase;
import com.example.homesweethome.ArtifactRepository;

public class AccessSingleton extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ArtifactDatabase getDatabase() { return ArtifactDatabase.getDatabase(getApplicationContext()); }
    public ArtifactRepository getRepository() { return ArtifactRepository.getInstance(getDatabase()); }
    public ImageProcessor getImageProcessor() { return ImageProcessor.getInstance(); }
}
