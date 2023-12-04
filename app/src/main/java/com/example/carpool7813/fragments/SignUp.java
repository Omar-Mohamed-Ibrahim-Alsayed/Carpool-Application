package com.example.carpool7813.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
import com.example.carpool7813.utilities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends Fragment {
    private FirebaseAuth mAuth;
    Button signUp, toggleUser;
    EditText e_et, p_et, pp_et, n_et;
    String email, password, name, password2, type;
    ProgressBar pb;
    boolean user_type = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

                if (!checkInput(name, email, password, password2)) {
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Set the display name here
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(type)
                                            .build();

                                    if (user != null) {
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                                        if (updateTask.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Successful", Toast.LENGTH_LONG).show();
                                                            databaseUpdate();
                                                            reload();

                                                        } else {
                                                            Toast.makeText(getContext(), "Failed to set display name", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                    }
                                } else {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                                signUp.setText("Sign Up");
                                pb.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });

        // Return the inflated view
        return view;
    }

    boolean checkInput(String name, String email, String password, String password2) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter Mail", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Enter pass", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (!password.equals(password2)) {
            Toast.makeText(getContext(), "Passwords" + password + "  do not match" + password2, Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (!email.endsWith("@eng.asu.edu.eg")) {
            Toast.makeText(getContext(), "Email must end with @eng.asu.edu.eg", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(getContext(), "Password must contain at least one uppercase character", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            Toast.makeText(getContext(), "Password must contain at least one special character", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(getContext(), "Password must contain at least one number", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Enter a valid email address", Toast.LENGTH_LONG).show();
            signUp.setText("Sign Up");
            pb.setVisibility(View.INVISIBLE);
            return false;
        }
        return true;
    }

    private void reload() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = mAuth.getCurrentUser();
        User newUser = new User(type, user.getEmail(), user.getDisplayName());

        String newUserId = usersRef.push().getKey();
        usersRef.child(newUserId).setValue(newUser);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Mail", user.getEmail());
        editor.apply();
        Intent intent;
        if (user_type) {
            intent = new Intent(getContext(), CustomerApp.class);
        } else {

            intent = new Intent(getContext(), DriverApp.class);
        }
        startActivity(intent);
    }

    private void databaseUpdate(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Email", email);
        user.put("Type", type);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Database Not updated", Toast.LENGTH_LONG).show();
                    }
                });
    }

}