package com.develop.dubhad.sdlab.authentication;

import com.develop.dubhad.sdlab.user.User;

public interface SignUpResultListener {
    void onSignUpSuccess(User user);
    void onUserExists();
    void onSignUpFail();
}
