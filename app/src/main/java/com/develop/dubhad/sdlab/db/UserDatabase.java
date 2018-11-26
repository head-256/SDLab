package com.develop.dubhad.sdlab.db;

import android.content.Context;
import android.os.AsyncTask;

import com.develop.dubhad.sdlab.user.User;
import com.develop.dubhad.sdlab.user.UserDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 3, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    
    public abstract UserDao userDao();
    
    private static volatile UserDatabase userDatabase;
    
    public static UserDatabase getUserDatabase(final Context context) {
        if (userDatabase == null) {
            synchronized (UserDatabase.class) {
                if (userDatabase == null) {
                    userDatabase = Room
                            .databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return userDatabase;
    }
    
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            
            new PopulateDbAsync(userDatabase).execute();
        }
    };
    
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        
        private final UserDao userDao;
        
        PopulateDbAsync(UserDatabase db) {
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            /*userDao.deleteAllUsers();
            User user = new User("GLEB", null, null, null, null);
            userDao.insertUser(user);
            user = new User("HLEB", null, null, null, null);
            userDao.insertUser(user);*/
            return null;
        }
    }
}
