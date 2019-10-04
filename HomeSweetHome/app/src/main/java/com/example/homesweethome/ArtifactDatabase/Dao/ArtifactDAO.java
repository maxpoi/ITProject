package com.example.homesweethome.ArtifactDatabase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;

import java.util.List;

@Dao
public interface ArtifactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artifact artifact);

    @Delete
    void delete(Artifact artifact);

    @Query("SELECT * FROM Artifacts WHERE id = :id")
    LiveData<Artifact> getArtifact(int id);

    @Query("SELECT * FROM artifacts")
    LiveData<List<Artifact>> getAllArtifacts();

    @Query("SELECT coverImagePath FROM artifacts")
    LiveData<List<String>> getCoverImagesPath();
}
