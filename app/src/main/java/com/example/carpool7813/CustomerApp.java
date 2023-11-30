package com.example.carpool7813;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.carpool7813.fragments.Cart;
import com.example.carpool7813.fragments.History;
import com.example.carpool7813.fragments.Profile;
import com.example.carpool7813.fragments.Routs;
import com.example.carpool7813.fragments.SignIn;
import com.example.carpool7813.fragments.SignUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerApp extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Routs routs = new Routs();
    Profile profile = new Profile();
    History history = new History();
    Cart cart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_app);
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.book) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, routs).commit();
                return true;
            } else if (itemId == R.id.cart) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, cart).commit();
                return true;
            } else if (itemId == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profile).commit();
                return true;
            } else if (itemId == R.id.history) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, history).commit();
                return true;
            }

            return false;
        });


        bottomNavigationView.setSelectedItemId(R.id.profile);
    }
}