package com.develop.dubhad.sdlab.authentication;

import com.develop.dubhad.sdlab.models.User;

public interface SignUpResultListener {
    void onSignUpSuccess(User user);
    void onUserExists();
    void onSignUpFail();
}
