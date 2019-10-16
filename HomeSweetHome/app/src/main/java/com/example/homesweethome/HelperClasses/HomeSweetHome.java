package com.example.homesweethome.HelperClasses;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.homesweethome.ArtifactDatabase.ArtifactDatabase;
import com.example.homesweethome.ArtifactRepository;

import java.util.Random;

public class HomeSweetHome extends Application {

    private SharedPreferences themePreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        ImageProcessor.getInstance(getApplicationContext().getFilesDir() + "/");

        themePreferences = getSharedPreferences("theme", MODE_PRIVATE);
        applyDarkMode();
    }

    public ArtifactDatabase getDatabase() { return ArtifactDatabase.getDatabase(getApplicationContext()); }
    public ArtifactRepository getRepository() { return ArtifactRepository.getInstance(getDatabase()); }
    public ImageProcessor getImageProcessor() { return ImageProcessor.getInstance(getApplicationContext().getFilesDir() + "/"); }

    public Boolean useDarkMode() { return themePreferences.getBoolean("dark", false); }
    public void setDarkMode(boolean dark) {
        themePreferences.edit().putBoolean("dark", dark).apply();
        applyDarkMode();
    }
    public void applyDarkMode() {
        if (themePreferences.getBoolean("dark", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
