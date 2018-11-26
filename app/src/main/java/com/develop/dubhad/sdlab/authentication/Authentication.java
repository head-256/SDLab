package com.develop.dubhad.sdlab.authentication;

import android.os.AsyncTask;

import com.develop.dubhad.sdlab.App;
import com.develop.dubhad.sdlab.user.User;
import com.develop.dubhad.sdlab.user.UserRepository;

public class Authentication {
    
    private static User currentUser;
    
    private static UserRepository userRepository = new UserRepository(App.getApp());
    
    private static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static boolean isAuthenticated() {
        return currentUser != null;
    }
    
    public static void signIn(SignInResultListener signInResultListener, String login, String password) {
        new signInAsyncTask(signInResultListener, userRepository).execute(login, password);
    }
    
    public static void signUp(SignUpResultListener signUpResultListener, String login, String password) {
        new signUpAsyncTask(signUpResultListener, userRepository).execute(login, password);
    }
    
    public static void logOut() {
        currentUser = null;
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

    private static class signUpAsyncTask extends AsyncTask<String, Void, User> {

        private UserRepository userRepository;

        private SignUpResultListener resultListener;

        signUpAsyncTask(SignUpResultListener signUpResultListener, UserRepository repository) {
            userRepository = repository;
            resultListener = signUpResultListener;
        }

        @Override
        protected User doInBackground(String... params) {
            User user = userRepository.getUser(params[0]);
            if (user == null) {
                user = new User(params[0], params[1]);
                userRepository.insertUser(user);
            }
            else {
                user = null;
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null) {
                resultListener.onUserExists();
            }
            else {
                resultListener.onSignUpSuccess(user);
            }
        }
    }
}
