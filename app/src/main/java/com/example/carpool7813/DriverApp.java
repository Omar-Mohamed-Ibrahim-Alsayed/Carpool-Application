package com.example.carpool7813;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverApp extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_app);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        webView = findViewById(R.id.webView);
        loadWebView("routs.html");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.Routs) {
                loadWebView("routs.html");
                return true;
            } else if (itemId == R.id.Requests) {
                loadWebView("requests.html");
                return true;
            } else if (itemId == R.id.profile) {
                loadWebView("profile.html");
                return true;
            } else if (itemId == R.id.Ratings) {
                loadWebView("rates.html");
                return true;
            }

            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.profile);
    }

    private void loadWebView(String fileName) {
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);
    }
}
