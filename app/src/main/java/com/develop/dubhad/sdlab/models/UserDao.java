package com.develop.dubhad.sdlab.models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    
    @Insert
    void insertUser(User user);
    
    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();
    
    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(int id);
}
