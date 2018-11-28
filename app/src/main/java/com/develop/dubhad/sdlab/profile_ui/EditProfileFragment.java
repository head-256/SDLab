package com.develop.dubhad.sdlab.profile_ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.develop.dubhad.sdlab.MainActivity;
import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.user.User;
import com.develop.dubhad.sdlab.user.UserViewModel;
import com.develop.dubhad.sdlab.util.ImageUtil;
import com.develop.dubhad.sdlab.util.KeyboardUtil;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class EditProfileFragment extends Fragment implements ConfirmChangesDialogFragment.ConfirmChangesDialogListener, 
        MainActivity.BackPressedListener {

    private EditText nameEditView;
    private EditText surnameEditView;
    private EditText phoneNumberEditView;
    private EditText emailEditView;
    private ImageView avatarEditView;

    private String avatarPath;

    private UserViewModel userViewModel;
    
    private HashMap<String, String> initialProfileData;

    private View.OnClickListener confirmProfileEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            KeyboardUtil.hideKeyboard(requireActivity());
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

        setHasOptionsMenu(true);
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

        initialProfileData = new HashMap<>();

        fillEditProfileData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isProfileDataChanged(initialProfileData, getCurrentProfileData())) {
                    KeyboardUtil.hideKeyboard(requireActivity());
                    showConfirmChangesDialog();
                    return true;
                }
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isProfileDataChanged(initialProfileData, getCurrentProfileData())) {
            KeyboardUtil.hideKeyboard(requireActivity());
            showConfirmChangesDialog();
        }
        else {
            Navigation.findNavController(getView()).navigateUp();
        }
    }

    @Override
    public void onDialogPositiveClick() {
        confirmProfileEdit(getView());
    }

    @Override
    public void onDialogNegativeClick() {
        Navigation.findNavController(getView()).navigateUp();
    }

    @Override
    public void onDialogCancelClick() {
        
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
        
        initialProfileData.put("name", name);
        initialProfileData.put("surname", surname);
        initialProfileData.put("phone", phone);
        initialProfileData.put("email", email);
        initialProfileData.put("picture", avatarPath);
        
        nameEditView.setText(name);
        surnameEditView.setText(surname);
        phoneNumberEditView.setText(phone);
        emailEditView.setText(email);

        ImageUtil.loadImage(getContext(), avatarPath, avatarEditView);
    }
    
    private void showConfirmChangesDialog() {
        DialogFragment fragment = new ConfirmChangesDialogFragment();
        fragment.setTargetFragment(this, 300);
        fragment.show(getFragmentManager(), "confirm");
    }
    
    private HashMap<String, String> getCurrentProfileData() {
        HashMap<String, String> currentProfileData = new HashMap<>();
        currentProfileData.put("name", nameEditView.getText().toString());
        currentProfileData.put("surname", surnameEditView.getText().toString());
        currentProfileData.put("phone", phoneNumberEditView.getText().toString());
        currentProfileData.put("email", emailEditView.getText().toString());
        currentProfileData.put("picture", avatarPath);
        return currentProfileData;
    }
    
    private boolean isProfileDataChanged(HashMap<String, String> oldData, HashMap<String, String> newData) {
        return !oldData.equals(newData);
    }
}
