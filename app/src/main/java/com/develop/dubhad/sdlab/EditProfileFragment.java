package com.develop.dubhad.sdlab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
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
    private ImageView avatarEditView;
    private String avatarPath;

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

        avatarEditView = view.findViewById(R.id.avatarEditView);
        avatarEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(EditProfileFragment.this)
                        .single()
                        .start();
            }
        });
        fillEditProfileData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            avatarPath = image.getPath();
            Toast.makeText(getContext(), image.getName(), Toast.LENGTH_SHORT).show();

            Glide.with(this)
                    .load(avatarPath)
                    .apply(RequestOptions.centerCropTransform())
                    .into(avatarEditView);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveProfileData() {
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.name_field_key), nameEditView.getText().toString());
        editor.putString(getString(R.string.surname_field_key), surnameEditView.getText().toString());
        editor.putString(getString(R.string.phone_field_key), phoneNumberEditView.getText().toString());
        editor.putString(getString(R.string.email_field_key), emailEditView.getText().toString());
        editor.putString(getString(R.string.avatar_field_key), avatarPath);
        editor.apply();
    }

    private void fillEditProfileData() {
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        String name = sharedPref.getString(getString(R.string.name_field_key), "");
        String surname = sharedPref.getString(getString(R.string.surname_field_key), "");
        String phone = sharedPref.getString(getString(R.string.phone_field_key), "");
        String email = sharedPref.getString(getString(R.string.email_field_key), "");
        avatarPath = sharedPref.getString(getString(R.string.avatar_field_key),
                Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.peka).toString());

        nameEditView.setText(name);
        surnameEditView.setText(surname);
        phoneNumberEditView.setText(phone);
        emailEditView.setText(email);

        Glide.with(this)
                .load(avatarPath)
                .apply(RequestOptions.centerCropTransform())
                .into(avatarEditView);
    }
}
