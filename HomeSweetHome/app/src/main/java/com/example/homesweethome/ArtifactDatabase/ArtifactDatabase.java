package com.example.homesweethome.ArtifactDatabase;

import android.content.Context;

import androidx.room.*;

import com.example.homesweethome.ArtifactDatabase.Dao.ArtifactDAO;
import com.example.homesweethome.ArtifactDatabase.Dao.ImageDAO;
import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;

@Database(entities = {Artifact.class, Image.class}, version = 1)
public abstract class ArtifactDatabase extends RoomDatabase {
    public abstract ArtifactDAO artifactDAO();
    public abstract ImageDAO imageDAO();

    private static volatile ArtifactDatabase  INSTANCE;

    public static ArtifactDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArtifactDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArtifactDatabase.class, "artifact_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
