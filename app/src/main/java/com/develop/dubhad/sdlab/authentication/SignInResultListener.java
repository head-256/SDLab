package com.develop.dubhad.sdlab.authentication;

import com.develop.dubhad.sdlab.user.User;

public interface SignInResultListener {
    void onSignInComplete(User user);
    void onSignInFail();
}
