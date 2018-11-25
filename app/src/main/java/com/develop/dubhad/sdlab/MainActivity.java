package com.develop.dubhad.sdlab;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.develop.dubhad.sdlab.Util.ImageUtil;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView headerLoginView;
    private TextView headerEmailView;
    private CircleImageView headerCircleView;
    
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setupDrawerHeader();
            }
        });

        setupNavigation();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(drawerLayout, Navigation.findNavController(this, R.id.nav_host_fragment));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void setupNavigation() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (!Authentication.isAuthenticated()) {
                            Snackbar.make(findViewById(android.R.id.content), "Sign in, please", Snackbar.LENGTH_SHORT).show();
                            return false;
                        } else {
                            menuItem.setChecked(true);
                            switch (menuItem.getItemId()) {
                                case R.id.profileFragment:
                                    navController.navigate(R.id.profileFragment);
                                    break;
                                case R.id.firstEmptyFragment:
                                    navController.navigate(R.id.firstEmptyFragment);
                                    break;
                                case R.id.secondEmptyFragment:
                                    navController.navigate(R.id.secondEmptyFragment);
                                    break;
                            }
                            drawerLayout.closeDrawers();
                            return true;
                        }
                    }
                });

        //NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupDrawerHeader() {
        User currentUser = Authentication.getCurrentUser();
        if (currentUser == null) {
            signInButton = navigationView.getHeaderView(0).findViewById(R.id.sign_in);
            signUpButton = navigationView.getHeaderView(0).findViewById(R.id.sign_up);
            
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
                    drawerLayout.closeDrawers();
                }
            });
            
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signUpFragment);
                    drawerLayout.closeDrawers();
                }
            });
            
            return;
        }

        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        navigationView.inflateHeaderView(R.layout.drawer_header);
        
        headerLoginView = navigationView.getHeaderView(0).findViewById(R.id.headerNameView);
        headerEmailView = navigationView.getHeaderView(0).findViewById(R.id.headerEmailView);
        headerCircleView = navigationView.getHeaderView(0).findViewById(R.id.headerCircleView);
        
        String login = currentUser.getLogin();
        String email = currentUser.getEmail();
        String avatar = currentUser.getPicture();

        headerLoginView.setText(login);
        headerEmailView.setText(email);

        ImageUtil.loadImage(this, avatar, headerCircleView);
    }
}
