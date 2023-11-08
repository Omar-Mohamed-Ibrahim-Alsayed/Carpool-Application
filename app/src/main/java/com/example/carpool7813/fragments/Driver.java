package com.example.carpool7813.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.carpool7813.R;

public class Driver extends Fragment {

    WebView myWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver, container, false);
        myWebView = view.findViewById(R.id.webView);
        myWebView.loadUrl("file:///android_asset/driver.html");

        return view;
    }
}
