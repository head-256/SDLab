package com.develop.dubhad.sdlab.user;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    
    @Insert
    void insertUser(User user);
    
    @Query("DELETE FROM User")
    void deleteAllUsers();
    
    @Update
    void updateUser(User user);
    
    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();
    
    @Query("SELECT * FROM User WHERE login = :login AND password = :password")
    User getUser(String login, String password);
    
    @Query("SELECT * FROM User WHERE login = :login")
    User getUser(String login);
    
    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(int id);
}
