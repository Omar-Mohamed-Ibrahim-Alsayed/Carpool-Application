package com.example.carpool7813;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.carpool7813.fragments.Cart;
import com.example.carpool7813.fragments.Driver;
import com.example.carpool7813.fragments.History;
import com.example.carpool7813.fragments.Order;
import com.example.carpool7813.fragments.Routs;
import com.example.carpool7813.fragments.SignIn;
import com.example.carpool7813.fragments.SignUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;


public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Routs routs = new Routs();
    SignIn signin = new SignIn();
    SignUp signup = new SignUp();
    History history = new History();
    Cart cart = new Cart();
    Driver driver = new Driver();

    Order order = new Order();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
//            fragmentManager.beginTransaction()
//                    .replace(R.id.flFragment, Cart.class, null)
//                    .setReorderingAllowed(true)
//                    .addToBackStack("name") // Name can be null
//                    .commit();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, signin).commit();
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
