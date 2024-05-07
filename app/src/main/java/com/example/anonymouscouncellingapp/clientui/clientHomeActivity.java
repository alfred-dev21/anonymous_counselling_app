package com.example.anonymouscouncellingapp.clientui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.anonymouscouncellingapp.LoginActivity;
import com.example.anonymouscouncellingapp.R;
import com.example.anonymouscouncellingapp.clientui.menuitems.About;
import com.example.anonymouscouncellingapp.clientui.menuitems.Home;
import com.example.anonymouscouncellingapp.clientui.menuitems.Profile;
import com.example.anonymouscouncellingapp.clientui.menuitems.Settings;
import com.example.anonymouscouncellingapp.clientui.menuitems.Share;
import com.example.anonymouscouncellingapp.clientui.menuitems.TermsAndConditions;
import com.google.android.material.navigation.NavigationView;

public class clientHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new Home())
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_menuItem:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Home())
                        .commit();
                break;
            case R.id.profile_menuItem:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Profile())
                        .commit();
                break;
            case R.id.share_menuItem:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Share())
                        .commit();
                break;
            case R.id.setting_menuItem:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Settings())
                        .commit();
                break;
            case R.id.t_and_c:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new TermsAndConditions())
                        .commit();
                break;
            case R.id.about_menuItem:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new About())
                        .commit();
                break;
            case R.id.logout_menuItem:
                Intent intent = new Intent(clientHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}