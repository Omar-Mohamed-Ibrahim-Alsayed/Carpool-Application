package com.example.carpool7813.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpool7813.CustomerApp;
import com.example.carpool7813.DriverApp;
import com.example.carpool7813.R;
import com.example.carpool7813.ViewModel.userViewModel;
import com.example.carpool7813.interfaces.SigninCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.model.userProfile;
import com.example.carpool7813.utilities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignIn extends Fragment implements SigninCallback {
    TextView signUpLink;
    SignUp su;
    FragmentManager parentFragmentManager;
    Button signIn_button;
    private FirebaseAuth mAuth;
    String email,password,user_type;
    EditText email_et;
    EditText password_et;
    ProgressBar pb;
    User user;
    FirebaseHandler authManager;
    userViewModel userViewModel;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        authManager = FirebaseHandler.getInstance();
        if (currentUser != null) {

            authManager.getUser(this);
            //reload();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();
        signUpLink = view.findViewById(R.id.signUpLink);
        su = new SignUp();
        parentFragmentManager = getParentFragmentManager();
        email_et = view.findViewById(R.id.usernameEditText);
        password_et = view.findViewById(R.id.passwordEditText);
        pb = view.findViewById(R.id.progressBar);



        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragmentManager.beginTransaction().replace(R.id.flFragment, su).addToBackStack(null).commit();
            }
        });

        signIn_button = view.findViewById(R.id.signInButton);
        signIn_button.setOnClickListener(v -> {
                    email = String.valueOf(email_et.getText());
                    password = String.valueOf(password_et.getText());
                    signIn_button.setText(" ");
                    pb.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getContext(), "Enter Mail", Toast.LENGTH_LONG).show();
                        signIn_button.setText("Sign In");
                        pb.setVisibility(View.INVISIBLE);
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getContext(), "Enter pass", Toast.LENGTH_LONG).show();
                        signIn_button.setText("Sign In");
                        pb.setVisibility(View.INVISIBLE);
                        return;
                    }

                    handleInfo();


                }
        );


        return view;
    }


    @Override
    public void onUserReceived(User user) {
        if(user != null){
            addUser(user);
            signIn_button.setText("Sign In");
            pb.setVisibility(View.INVISIBLE);
            reload();
        }
    }

    public void handleInfo(){
        authManager.loginUser(this ,getContext(),email,password);
        authManager.getUser(this);
    }


    private void reload() {
        FirebaseUser user = mAuth.getCurrentUser();
        Context context = getActivity();
        user_type = user.getDisplayName();
        Intent intent;
        if (user_type.equals("Client")) {
            intent = new Intent(context, CustomerApp.class);
            startActivity(intent);
        } else if(user_type.equals("Driver")) {
            intent = new Intent(context, DriverApp.class);
            startActivity(intent);
        }
        else{

        }
    }

    void addUser(User user2) {
        userProfile user = new userProfile(user2.getId(),user2.getName(),user2.getEmail(),user2.getUserType());
        userViewModel = new ViewModelProvider(this).get(userViewModel.class);
        userViewModel.insertUser(user);

    }

}


