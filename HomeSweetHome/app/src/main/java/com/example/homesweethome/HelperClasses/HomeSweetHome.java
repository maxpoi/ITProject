package com.example.homesweethome.HelperClasses;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.homesweethome.AppDataBase.AppDatabase;
import com.example.homesweethome.AppRepository;

public class HomeSweetHome extends Application {

    private SharedPreferences themePreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        ImageProcessor.getInstance(getApplicationContext().getFilesDir() + "/");

        themePreferences = getSharedPreferences("theme", MODE_PRIVATE);
        applyDarkMode();
    }

    public AppDatabase getDatabase() { return AppDatabase.getDatabase(getApplicationContext()); }
    public AppRepository getRepository() { return AppRepository.getInstance(getDatabase()); }
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
