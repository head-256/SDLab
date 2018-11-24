package com.develop.dubhad.sdlab;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.authentication.SignInResultListener;
import com.develop.dubhad.sdlab.models.User;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SignInFragment extends Fragment implements SignInResultListener {
    
    private TextInputEditText inputLogin;
    private TextInputEditText inputPassword;
    
    private Button signInButton;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        inputLogin = view.findViewById(R.id.input_login);
        inputPassword = view.findViewById(R.id.input_password);
        signInButton = view.findViewById(R.id.btn_sign_in);
        
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputLogin.getText()) && !TextUtils.isEmpty(inputPassword.getText())) {
                    Authentication.signIn(SignInFragment.this, inputLogin.getText().toString(), inputPassword.getText().toString());
                }
            }
        });
    }

    @Override
    public void onSignInComplete(User user) {
        Navigation.findNavController(getView()).navigate(R.id.profileFragment);
        Log.d("RESULT", "SUCCESS");
    }

    @Override
    public void onSignInFail() {
        Log.d("RESULT", "FAIL");
    }
}
