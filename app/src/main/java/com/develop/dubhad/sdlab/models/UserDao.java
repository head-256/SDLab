package com.develop.dubhad.sdlab.models;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    
    @Insert
    void insertUser(User user);
    
    @Query("SELECT * FROM User")
    List<User> getAllUsers();
    
    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(int id);
}
