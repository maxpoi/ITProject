package com.example.homesweethome.AppDataBase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.homesweethome.AppDataBase.Entities.Artifact;

import java.util.List;

@Dao
public interface ArtifactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artifact artifact);

    @Delete
    void delete(Artifact artifact);

    @Query("SELECT * FROM Artifacts WHERE id = :id")
    LiveData<Artifact> getArtifact(int id);

    @Query("SELECT * FROM Artifacts WHERE id = :id")
    Artifact getStaticArtifact(int id);

    @Query("SELECT id FROM Artifacts ORDER BY id DESC LIMIT 1")
    int getLastArtifactId();

    @Query("SELECT * FROM artifacts ORDER BY id DESC")
    LiveData<List<Artifact>> getAllArtifacts();

    @Query("SELECT * FROM artifacts ORDER BY id DESC")
    List<Artifact> getAllStaticArtifacts();

    @Query("SELECT coverImagePath FROM artifacts")
    LiveData<List<String>> getCoverImagesPath();

    @Query("SELECT Artifacts.* " +
            "FROM Artifacts JOIN ArtifactFts ON (Artifacts.id = ArtifactFts.rowid) " +
            "WHERE ArtifactFts MATCH :query")
    LiveData<List<Artifact>> searchAllArtifacts(String query);
}
