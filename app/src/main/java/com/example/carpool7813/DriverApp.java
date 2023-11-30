package com.example.carpool7813;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.carpool7813.fragments.Cart;
import com.example.carpool7813.fragments.History;
import com.example.carpool7813.fragments.Routs;
import com.example.carpool7813.fragments.SignIn;
import com.example.carpool7813.fragments.SignUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverApp extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_app);
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.Routs) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, routs).commit();
                return true;
            } else if (itemId == R.id.Requests) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, cart).commit();
                return true;
            } else if (itemId == R.id.profile) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, signin).commit();
                return true;
            } else if (itemId == R.id.Ratings) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, history).commit();
                return true;
            }

            return false;
        });


        bottomNavigationView.setSelectedItemId(R.id.profile);
    }
}