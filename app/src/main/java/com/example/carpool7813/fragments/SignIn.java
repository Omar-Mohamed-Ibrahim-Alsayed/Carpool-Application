package com.example.carpool7813.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpool7813.MainActivity;
import com.example.carpool7813.R;


public class SignIn extends Fragment  {
    TextView signUpLink;
    SignUp su;
    FragmentManager parentFragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        signUpLink = (TextView) view.findViewById(R.id.signUpLink);
        su = new SignUp();
        parentFragmentManager = getParentFragmentManager();
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragmentManager.beginTransaction().replace(R.id.flFragment, su).commit();
                signUpLink.setText('H');
            }
        });
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

}