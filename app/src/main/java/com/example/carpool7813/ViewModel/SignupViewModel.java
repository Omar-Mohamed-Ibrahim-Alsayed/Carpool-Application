package com.example.carpool7813.ViewModel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignupViewModel {
    private static SignupViewModel instance;

    private SignupViewModel() {

    }

    public static synchronized SignupViewModel getInstance() {
        if (instance == null) {
            instance = new SignupViewModel();
        }
        return instance;
    }

    public boolean checkInput(Context context, String name, String email, String password, String password2) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter Name", Toast.LENGTH_LONG).show();

            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter Mail", Toast.LENGTH_LONG).show();

            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter pass", Toast.LENGTH_LONG).show();

            return false;
        }
        if (!password.equals(password2)) {
            Toast.makeText(context, "Passwords" + password + "  do not match" + password2, Toast.LENGTH_LONG).show();

            return false;
        }
        if (!email.endsWith("@eng.asu.edu.eg")) {
            Toast.makeText(context, "Email must end with @eng.asu.edu.eg", Toast.LENGTH_LONG).show();

            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(context, "Password must contain at least one uppercase character", Toast.LENGTH_LONG).show();

            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            Toast.makeText(context, "Password must contain at least one special character", Toast.LENGTH_LONG).show();

            return false;
        }
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(context, "Password must contain at least one number", Toast.LENGTH_LONG).show();

            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }

}
