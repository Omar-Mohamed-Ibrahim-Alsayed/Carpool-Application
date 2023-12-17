package com.example.carpool7813.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carpool7813.R;
import com.example.carpool7813.interfaces.SigninCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment implements SigninCallback {
    Button sign_out_button;
    FragmentManager parentFragmentManager;
    private FirebaseAuth mAuth;
    SignIn sign_in_page;
    TextView welcome_tx, mail_tx;
    FirebaseFirestore db;
    ProgressBar pb;
    FirebaseHandler fb;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        pb.setVisibility(View.VISIBLE);
        fb = FirebaseHandler.getInstance();
        fb.getUser(this);



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
        pb = view.findViewById(R.id.progressBar);

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


    @Override
    public void onUserReceived(User user) {
        if(user !=null){
            welcome_tx.setText(getString(R.string.profile_title) + " " + user.getName());
            mail_tx.setText(user.getEmail());
            pb.setVisibility(View.INVISIBLE);

        }else {
            // Handle failure to retrieve user data
            welcome_tx.setText(getString(R.string.profile_title) + " " + "Not Found");
            mail_tx.setText("NOT FOUND");

            pb.setVisibility(View.INVISIBLE);
        }
    }
}