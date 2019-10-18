package com.example.homesweethome.AppDataBase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.homesweethome.AppDataBase.Entities.Image;

import java.util.List;

@Dao
public interface ImageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Insert
    void  insertAll(List<Image> images);

    @Delete
    void delete(Image image);

    @Query("DELETE FROM Images WHERE artifactId = :artifactId")
    void deleteAllArtifactImages(int artifactId);

    @Query("SELECT * FROM Images WHERE artifactId = :artifactId")
    LiveData<List<Image>> getImages(int artifactId);

    @Query("SELECT * FROM Images WHERE artifactId = :artifactId")
    List<Image> getStaticImages(int artifactId);

    @Query("SELECT id FROM Images WHERE artifactId = :artifactId ORDER BY id DESC LIMIT 1")
    int getLastImageId(int artifactId);
}
