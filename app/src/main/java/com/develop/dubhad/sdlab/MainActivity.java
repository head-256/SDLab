package com.develop.dubhad.sdlab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.develop.dubhad.sdlab.Util.ImageUtil;
import com.develop.dubhad.sdlab.authentication.Authentication;
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

    private TextView headerNameView;
    private TextView headerEmailView;
    private CircleImageView headerCircleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        headerNameView = navigationView.getHeaderView(0).findViewById(R.id.headerNameView);
        headerEmailView = navigationView.getHeaderView(0).findViewById(R.id.headerEmailView);
        headerCircleView = navigationView.getHeaderView(0).findViewById(R.id.headerCircleView);

        setupNavigation();

        setupDrawerHeader();
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
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String name = sharedPref.getString(getString(R.string.name_field_key), "");
        String email = sharedPref.getString(getString(R.string.email_field_key), "");
        String avatar = sharedPref.getString(getString(R.string.avatar_field_key), ImageUtil.DEFAULT_IMAGE_PATH);

        headerNameView.setText(name);
        headerEmailView.setText(email);

        ImageUtil.loadImage(this, avatar, headerCircleView);
    }
}
