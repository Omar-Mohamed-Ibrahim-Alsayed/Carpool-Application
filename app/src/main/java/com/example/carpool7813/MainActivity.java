package com.example.carpool7813;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carpool7813.fragments.SignIn;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    SignIn si = new SignIn();
    String user_type;
    @Override
    public void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            user_type = currentUser.getDisplayName();
//            Intent intent;
//            if (user_type.equals("Client")) {
//                intent = new Intent(this, CustomerApp.class);
//                startActivity(intent);
//            } else if (user_type.equals("Driver")){
//                intent = new Intent(this, DriverApp.class);
//                startActivity(intent);
//            }
//
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            FirebaseApp.initializeApp(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, si).commit();

    }




}
