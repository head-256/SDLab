package com.develop.dubhad.sdlab.user;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {
    
    private UserRepository userRepository;
    
    private LiveData<List<User>> users;
    
    public UserViewModel(Application app) {
        super(app);
        
        userRepository = new UserRepository(app);
        users = userRepository.getAllUsers();
    }
    
    public LiveData<List<User>> getAllUsers() {
        return users;
    }
    
    public void insertUser(User user) {
        userRepository.insertUser(user);
    }
    
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
}
