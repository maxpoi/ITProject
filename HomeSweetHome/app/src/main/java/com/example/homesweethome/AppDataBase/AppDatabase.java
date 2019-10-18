package com.example.homesweethome.AppDataBase;

import android.content.Context;

import androidx.room.*;

import com.example.homesweethome.AppDataBase.Dao.ArtifactDAO;
import com.example.homesweethome.AppDataBase.Dao.ImageDAO;
import com.example.homesweethome.AppDataBase.Dao.UserDAO;
import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.AppDataBase.Entities.ArtifactFts;
import com.example.homesweethome.AppDataBase.Entities.Image;
import com.example.homesweethome.AppDataBase.Entities.User;

@Database(entities = {Artifact.class, ArtifactFts.class, Image.class, User.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArtifactDAO artifactDAO();
    public abstract ImageDAO imageDAO();
    public abstract UserDAO userDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
