package com.example.carpool7813.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.example.carpool7813.MainActivity;
import com.example.carpool7813.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class SignIn extends Fragment {
    TextView signUpLink;
    SignUp su;
    FragmentManager parentFragmentManager;
    Button signIn_button;
    private FirebaseAuth mAuth;
    String email,password,user_type;
    EditText email_et;
    EditText password_et;
    ProgressBar pb;

    private void reload() {
        FirebaseUser user = mAuth.getCurrentUser();
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Mail", user.getEmail());
        editor.putString("Type", user.getDisplayName());
        editor.apply();
        user_type = user.getDisplayName();
        Intent intent;
        if (user_type.equals("Client")) {
            intent = new Intent(context, CustomerApp.class);
        } else {
            intent = new Intent(context, DriverApp.class);
        }
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            reload();

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
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        reload();

                                    } else {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();


                                    }
                                    signIn_button.setText("Sign Up");
                                    pb.setVisibility(View.INVISIBLE);

                                }

                            });

                }
        );


        return view; // Return the inflated view instead of re-inflating the layout
    }
}


