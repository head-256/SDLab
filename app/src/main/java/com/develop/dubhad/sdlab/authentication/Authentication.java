package com.develop.dubhad.sdlab.authentication;

import android.os.AsyncTask;

import com.develop.dubhad.sdlab.App;
import com.develop.dubhad.sdlab.models.User;
import com.develop.dubhad.sdlab.models.UserRepository;

public class Authentication {
    
    private static User currentUser;
    
    private static UserRepository userRepository = new UserRepository(App.getApp());
    
    private static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void signIn(SignInResultListener signInResultListener, String login, String password) {
        new signInAsyncTask(signInResultListener, userRepository).execute(login, password);
    }
    
    private static class signInAsyncTask extends AsyncTask<String, Void, User> {
        
        private UserRepository userRepository;
        
        private SignInResultListener resultListener;
        
        signInAsyncTask(SignInResultListener signInResultListener, UserRepository repository) {
            userRepository = repository;
            resultListener = signInResultListener;
        }

        @Override
        protected User doInBackground(String... params) {
            return userRepository.getUser(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null) {
                resultListener.onSignInFail();
            }
            else {
                setCurrentUser(user);
                resultListener.onSignInComplete(user);
            }
        }
    }
}
