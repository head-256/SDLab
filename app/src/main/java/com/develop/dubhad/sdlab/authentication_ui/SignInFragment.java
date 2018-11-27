package com.develop.dubhad.sdlab.authentication_ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.authentication.SignInResultListener;
import com.develop.dubhad.sdlab.user.User;
import com.develop.dubhad.sdlab.util.KeyboardUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SignInFragment extends Fragment implements SignInResultListener {
    
    private TextInputEditText inputLogin;
    private TextInputEditText inputPassword;
    
    private TextInputLayout loginLayout;
    private TextInputLayout passwordLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        inputLogin = view.findViewById(R.id.input_login);
        inputPassword = view.findViewById(R.id.input_password);
        Button signInButton = view.findViewById(R.id.btn_sign_in);
        
        loginLayout = view.findViewById(R.id.input_login_layout);
        passwordLayout = view.findViewById(R.id.input_password_layout);
        
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.hideKeyboard(requireActivity());
                
                loginLayout.setError(null);
                passwordLayout.setError(null);
                if (TextUtils.isEmpty(inputLogin.getText())) {
                    loginLayout.setError(getString(R.string.empty_login_error));
                }
                if (TextUtils.isEmpty(inputPassword.getText())) {   
                    passwordLayout.setError(getString(R.string.empty_password_error));
                }
                else {
                    Authentication.signIn(SignInFragment.this, inputLogin.getText().toString(), inputPassword.getText().toString());
                }
            }
        });
    }

    @Override
    public void onSignInComplete(User user) {
        Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_profileFragment);
        Snackbar.make(getView(), getString(R.string.login_complete_message, user.getLogin()), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSignInFail() {
        loginLayout.setError(getString(R.string.wrong_data_error));
        passwordLayout.setError(getString(R.string.wrong_data_error));
    }
}
