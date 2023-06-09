package com.example.nasa_images;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**

 The BaseActivity class is used to provide a common implementation for all activities
 in the application. It extends the AppCompatActivity class and implements the
 NavigationView.OnNavigationItemSelectedListener interface to allow for navigation through
 the app using the navigation drawer. The class contains methods to set up the toolbar,
 add menu items to the toolbar, handle menu item selection, and handle navigation item
 selection. This class should be extended by all activities in the application that use
 the navigation drawer for navigation.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.tlB1);
        setSupportActionBar(myToolbar);
        drawerLayout = findViewById(R.id.drawerL);
        ActionBarDrawerToggle Toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item1:
                Toast.makeText(this, "Thanks for a great semester!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item2:
                Toast.makeText(this, "Thank you for being accommodating!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.nav_nasa_image:
                startActivity(new Intent(this, NASAList.class));
                break;

            case R.id.nav_about:
                startActivity(new Intent(this, HowActivity.class));

            case R.id.nav_exit:
                finishAffinity();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}