package com.develop.dubhad.sdlab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.develop.dubhad.sdlab.Util.ImageUtil;
import com.develop.dubhad.sdlab.models.User;
import com.develop.dubhad.sdlab.models.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class ProfileFragment extends Fragment {

    private TextView nameView;
    private TextView surnameView;
    private TextView phoneNumberView;
    private TextView emailView;
    private ImageView avatarView;
    
    private UserViewModel userViewModel;

    private View.OnClickListener editProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigate(R.id.editProfileFragment);
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

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        
        nameView = view.findViewById(R.id.nameView);
        surnameView = view.findViewById(R.id.surnameView);
        phoneNumberView = view.findViewById(R.id.phoneNumberView);
        emailView = view.findViewById(R.id.emailView);
        avatarView = view.findViewById(R.id.avatarView);

        FloatingActionButton editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(editProfileListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        fillProfileData();
    }

    private void fillProfileData() {
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable final List<User> users) {
                User currentUser = users.get(0);
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
        });
    }
}
