package com.develop.dubhad.sdlab.models;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {
    
    private UserDao userDao;
    
    private LiveData<List<User>> users;
    
    public UserRepository(Application app) {
        UserDatabase db = UserDatabase.getUserDatabase(app);
        userDao = db.userDao();
        users = userDao.getAllUsers();
    }
    
    public LiveData<List<User>> getAllUsers() {
        return users;
    }
    
    public void insertUser(User user) {
        new insertUserAsyncTask(userDao).execute(user);
    }
    
    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {
        
        private UserDao asyncTaskDao;
        
        insertUserAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            asyncTaskDao.insertUser(users[0]);
            return null;
        }
    }
}
