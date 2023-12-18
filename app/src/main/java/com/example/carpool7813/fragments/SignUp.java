package com.example.carpool7813.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.carpool7813.CustomerApp;
import com.example.carpool7813.DriverApp;
import com.example.carpool7813.R;
import com.example.carpool7813.ViewModel.SignupViewModel;
import com.example.carpool7813.ViewModel.userViewModel;
import com.example.carpool7813.interfaces.SignUpCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.model.userProfile;
import com.example.carpool7813.utilities.User;


public class SignUp extends Fragment implements SignUpCallback {

    Button signUp, toggleUser;
    EditText e_et, p_et, pp_et, n_et;
    String email, password, name, password2, type;
    ProgressBar pb;
    boolean user_type = true;
    SignupViewModel suvm;
    //FirebaseAuthManager auth;
    FirebaseHandler auth;
    userViewModel userViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        e_et = view.findViewById(R.id.emailEditText);
        p_et = view.findViewById(R.id.passwordEditText);
        pp_et = view.findViewById(R.id.password2EditText);
        n_et = view.findViewById(R.id.usernameEditText);
        signUp = view.findViewById(R.id.signUpButton);
        toggleUser = view.findViewById(R.id.userTypeSwitch);
        pb = view.findViewById(R.id.progressBar);
        suvm = SignupViewModel.getInstance();
        auth = FirebaseHandler.getInstance();

        toggleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_type = !user_type;
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = e_et.getText().toString();
                password = p_et.getText().toString().trim();
                name = n_et.getText().toString();
                password2 = pp_et.getText().toString().trim();
                signUp.setText(" ");
                pb.setVisibility(View.VISIBLE);
                if (user_type) {
                    type = "Client";
                } else {
                    type = "Driver";
                }

                if (!suvm.checkInput(getContext(),name, email, password, password2)) {
                    return;
                }
                handleInfo(name,email,password,type);


            }
        });

        // Return the inflated view
        return view;
    }

    public void handleInfo(String name, String email, String password, String type) {
        auth.registerUser(this,getActivity(),getContext(),name,email,password,type);

    }

    @Override
    public void onUserReceived(User user) {
        Log.e("INNNN ADD USER",user.getId());
        auth.addUser(user);
        addUser(user);

        reload();
    }
    void addUser(User user2) {
        userProfile user = new userProfile(user2.getId(),user2.getName(),user2.getEmail(),type);
        userViewModel = new ViewModelProvider(this).get(userViewModel.class);
        userViewModel.insertUser(user);

    }


    private void reload() {
        Intent intent;
        if (user_type) {
            intent = new Intent(getContext(), CustomerApp.class);
        } else {

            intent = new Intent(getContext(), DriverApp.class);
        }
        startActivity(intent);
    }



}