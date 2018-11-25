package com.develop.dubhad.sdlab;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.authentication.SignUpResultListener;
import com.develop.dubhad.sdlab.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class SignUpFragment extends Fragment implements SignUpResultListener {
    
    private TextInputEditText inputLogin;
    private TextInputEditText inputPassword;
    private TextInputLayout loginLayout;
    
    private Button registerButton;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        inputLogin = view.findViewById(R.id.register_input_login);
        inputPassword = view.findViewById(R.id.register_input_password);
        loginLayout = view.findViewById(R.id.register_login_layout);
        
        registerButton = view.findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputLogin.getText()) && !TextUtils.isEmpty(inputPassword.getText())) {
                    Authentication.signUp(SignUpFragment.this, inputLogin.getText().toString(), inputPassword.getText().toString());
                }
            }
        });
    }

    @Override
    public void onSignUpSuccess(User user) {
        Navigation.findNavController(getView()).navigate(R.id.signInFragment);
    }

    @Override
    public void onUserExists() {
        loginLayout.setError("User already exists");
    }

    @Override
    public void onSignUpFail() {

    }
}
