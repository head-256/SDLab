package com.develop.dubhad.sdlab.profile_ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.util.ImageUtil;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.user.User;
import com.develop.dubhad.sdlab.user.UserViewModel;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class EditProfileFragment extends Fragment {

    private EditText nameEditView;
    private EditText surnameEditView;
    private EditText phoneNumberEditView;
    private EditText emailEditView;
    private ImageView avatarEditView;

    private String avatarPath;

    private UserViewModel userViewModel;

    private View.OnClickListener confirmProfileEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            confirmProfileEdit(view);
        }
    };

    private View.OnClickListener avatarEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pickImage();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.edit_profile_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        nameEditView = view.findViewById(R.id.nameEditView);
        surnameEditView = view.findViewById(R.id.surnameEditView);
        phoneNumberEditView = view.findViewById(R.id.phoneNumberEditView);
        emailEditView = view.findViewById(R.id.emailEditView);

        avatarEditView = view.findViewById(R.id.avatarEditView);
        avatarEditView.setOnClickListener(avatarEditListener);

        FloatingActionButton confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmProfileEditListener);

        fillEditProfileData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        handlePickedImage(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickImage() {
        ImagePicker.create(this)
                .single()
                .start();
    }

    private void handlePickedImage(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            avatarPath = image.getPath();
            ImageUtil.loadImage(getContext(), avatarPath, avatarEditView);
        }
    }

    private void confirmProfileEdit(View view) {
        saveProfileData();
        
        Bundle confirmBundle = new Bundle();
        confirmBundle.putBoolean(getString(R.string.profile_data_save_confirmed_key), true);

        NavController navController = Navigation.findNavController(view);
        navController.popBackStack(R.id.profileFragment, true);
        navController.navigate(R.id.profileFragment, confirmBundle);
    }

    private void saveProfileData() {
        User currentUser = Authentication.getCurrentUser();
        if (currentUser == null) return;

        currentUser.setName(nameEditView.getText().toString());
        currentUser.setSurname(surnameEditView.getText().toString());
        currentUser.setPhoneNumber(phoneNumberEditView.getText().toString());
        currentUser.setEmail(emailEditView.getText().toString());
        currentUser.setPicture(avatarPath);

        userViewModel.updateUser(currentUser);
    }

    private void fillEditProfileData() {
        User currentUser = Authentication.getCurrentUser();
        if (currentUser == null) return;
        
        String name = currentUser.getName();
        String surname = currentUser.getSurname();
        String phone = currentUser.getPhoneNumber();
        String email = currentUser.getEmail();
        avatarPath = currentUser.getPicture();
        if (avatarPath == null) {
            avatarPath = ImageUtil.DEFAULT_IMAGE_PATH;
        }

        nameEditView.setText(name);
        surnameEditView.setText(surname);
        phoneNumberEditView.setText(phone);
        emailEditView.setText(email);

        ImageUtil.loadImage(getContext(), avatarPath, avatarEditView);
    }
}
