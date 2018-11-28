package com.develop.dubhad.sdlab.profile_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.util.ImageUtil;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ProfileFragment extends Fragment {

    private TextView nameView;
    private TextView surnameView;
    private TextView phoneNumberView;
    private TextView emailView;
    private ImageView avatarView;

    private View.OnClickListener editProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editProfileFragment);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.profile_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        nameView = view.findViewById(R.id.nameView);
        surnameView = view.findViewById(R.id.surnameView);
        phoneNumberView = view.findViewById(R.id.phoneNumberView);
        emailView = view.findViewById(R.id.emailView);
        avatarView = view.findViewById(R.id.avatarView);

        FloatingActionButton editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(editProfileListener);
        
        if (getArguments() != null && getArguments().getBoolean(getString(R.string.profile_data_save_confirmed_key))) {
            Snackbar.make(view, getString(R.string.saved_message), Snackbar.LENGTH_LONG).show();
            getArguments().clear();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        fillProfileData();
    }

    private void fillProfileData() {
        User currentUser = Authentication.getCurrentUser();
        if (currentUser == null) return;
        
        String name = currentUser.getName();
        String surname = currentUser.getSurname();
        String phone = currentUser.getPhoneNumber();
        String email = currentUser.getEmail();
        String avatar = currentUser.getPicture();
        if (avatar == null) {
            avatar = ImageUtil.DEFAULT_IMAGE_PATH;
        }

        nameView.setText(name);
        surnameView.setText(surname);
        phoneNumberView.setText(phone);
        emailView.setText(email);

        ImageUtil.loadImage(getContext(), avatar, avatarView);
    }
}
