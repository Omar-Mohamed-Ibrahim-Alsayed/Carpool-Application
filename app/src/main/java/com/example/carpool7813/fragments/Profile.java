package com.example.carpool7813.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpool7813.R;
import com.example.carpool7813.interfaces.RepoCallBack;
import com.example.carpool7813.interfaces.SigninCallback;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.ViewModel.userViewModel;
import com.example.carpool7813.model.userProfile;
import com.example.carpool7813.utilities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Profile extends Fragment implements UserCallback {
    Button sign_out_button,pass;
    FragmentManager parentFragmentManager;
    private FirebaseAuth mAuth;
    SignIn sign_in_page;
    TextView welcome_tx, mail_tx;
    FirebaseFirestore db;
    ProgressBar pb;
    FirebaseHandler fb;
    User user;
    userViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        pb.setVisibility(View.VISIBLE);
        fb = FirebaseHandler.getInstance();
        userViewModel = new ViewModelProvider(this).get(userViewModel.class);
        //fb.getUser(this);
        getUser();
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
        pass = view.findViewById(R.id.bypass);

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.deleteAll();
                FirebaseAuth.getInstance().signOut();
                if (getActivity() != null) {
                    getActivity().finish();
                }

            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences =  requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                boolean yourBooleanValue = false;
                editor.putBoolean("bypass", yourBooleanValue);

                editor.apply();
                boolean retrievedBooleanValue = sharedPreferences.getBoolean("bypass", false);
                if(retrievedBooleanValue){
                Toast.makeText(getContext(),"Bypass is: " + "TRUE",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"Bypass is: " + "False",Toast.LENGTH_LONG).show();
                }

            }
        });

        // Return the inflated view
        return view;
    }

    void getUser() {
        userViewModel.getStudentFromVm(this);
    }

    @Override
    public void onCallback(LiveData<userProfile> userLiveData) {

    }

    @Override
    public void onSpecialCall(userProfile userProfile) {
        if (userProfile != null) {
            welcome_tx.setText(getString(R.string.profile_title) + " " + userProfile.getName());
            mail_tx.setText(userProfile.getEmail());
            pb.setVisibility(View.INVISIBLE);
        }else{
            Toast.makeText(getContext(),"USER NOT FOUND",Toast.LENGTH_LONG).show();
            getUser();
        }
    }
}