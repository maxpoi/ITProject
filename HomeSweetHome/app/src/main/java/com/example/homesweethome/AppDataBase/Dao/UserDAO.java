package com.example.homesweethome.AppDataBase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.homesweethome.AppDataBase.Entities.User;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE email = :userEmail")
    LiveData<User> getUser(String userEmail);

    @Query("SELECT * FROM User WHERE email = :userEmail")
    User getStaticUser(String userEmail);

    @Query("SELECT portraitImagePath FROM User WHERE email = :userEmail")
    LiveData<String> getPortraitImagePath(String userEmail);
}
