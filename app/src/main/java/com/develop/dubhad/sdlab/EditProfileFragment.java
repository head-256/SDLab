package com.develop.dubhad.sdlab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class EditProfileFragment extends Fragment {

    private EditText nameEditView;
    private EditText surnameEditView;
    private EditText phoneNumberEditView;
    private EditText emailEditView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditView = view.findViewById(R.id.nameEditView);
        surnameEditView = view.findViewById(R.id.surnameEditView);
        phoneNumberEditView = view.findViewById(R.id.phoneNumberEditView);
        emailEditView = view.findViewById(R.id.emailEditView);

        FloatingActionButton confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileData();
                Navigation.findNavController(view).navigateUp();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        fillEditProfileData();
    }

    private void saveProfileData() {
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.name_field_key), nameEditView.getText().toString());
        editor.putString(getString(R.string.surname_field_key), surnameEditView.getText().toString());
        editor.putString(getString(R.string.phone_field_key), phoneNumberEditView.getText().toString());
        editor.putString(getString(R.string.email_field_key), emailEditView.getText().toString());
        editor.apply();
    }

    private void fillEditProfileData() {
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        String name = sharedPref.getString(getString(R.string.name_field_key), "");
        String surname = sharedPref.getString(getString(R.string.surname_field_key), "");
        String phone = sharedPref.getString(getString(R.string.phone_field_key), "");
        String email = sharedPref.getString(getString(R.string.email_field_key), "");

        nameEditView.setText(name);
        surnameEditView.setText(surname);
        phoneNumberEditView.setText(phone);
        emailEditView.setText(email);
    }
}
