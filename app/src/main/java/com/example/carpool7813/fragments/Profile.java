package com.example.carpool7813.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpool7813.CustomerApp;
import com.example.carpool7813.MainActivity;
import com.example.carpool7813.R;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.utilities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {
    Button sign_out_button;
    FragmentManager parentFragmentManager;
    private FirebaseAuth mAuth;
    SignIn sign_in_page;
    TextView welcome_tx,mail_tx;
    User me;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        getUser(sharedPref.getString("Mail", "Not found"), new UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    me = user;
                    welcome_tx.setText(getString(R.string.profile_title) + " " + me.getName());
                    mail_tx.setText(me.getEmail());

                } else {
                    Toast.makeText(getContext(), "Failed to retrieve user data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sign_out_button = view.findViewById(R.id.signOutButton);
        mAuth = FirebaseAuth.getInstance();
        parentFragmentManager = getParentFragmentManager();
        sign_in_page = new SignIn();
        welcome_tx = view.findViewById(R.id.welcomeText);
        mail_tx = view.findViewById(R.id.mail_info);

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        // Return the inflated view
        return view;
    }


    public void getUser(String mail, UserCallback userCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Query query = myRef.orderByChild("email").equalTo(mail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String type = snapshot.child("userType").getValue(String.class);
                    currentUser = new User(type, email, name);
                }
                userCallback.onCallback(currentUser); // Pass the user data back using the callback
            }

            @Override
            public void onCancelled(DatabaseError error) {
                User errorUser = new User("ERROR", "ERROR", "ERROR");
                userCallback.onCallback(errorUser); // Pass error user data back using the callback
            }
        });
    }

}