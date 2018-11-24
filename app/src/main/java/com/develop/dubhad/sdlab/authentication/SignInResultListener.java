package com.develop.dubhad.sdlab.authentication;

import com.develop.dubhad.sdlab.models.User;

public interface SignInResultListener {
    void onSignInComplete(User user);
    void onSignInFail();
}
