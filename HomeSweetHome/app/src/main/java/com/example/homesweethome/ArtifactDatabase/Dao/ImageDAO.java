package com.example.homesweethome.ArtifactDatabase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.homesweethome.ArtifactDatabase.Entities.Image;

import java.util.List;

@Dao
public interface ImageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Insert
    void  insertAll(List<Image> images);

    @Delete
    void delete(Image image);

    @Query("SELECT * FROM Images WHERE artifactId = :artifactId")
    LiveData<List<Image>> getImages(int artifactId);
}