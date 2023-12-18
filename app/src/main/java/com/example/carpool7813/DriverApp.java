package com.example.carpool7813;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.carpool7813.ViewModel.userViewModel;
import com.example.carpool7813.interfaces.OrdersCallback;
import com.example.carpool7813.interfaces.RoutsCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DriverApp extends AppCompatActivity implements OrdersCallback {
    BottomNavigationView bottomNavigationView;
    WebView webView;
    userViewModel userViewModel;
    String userName,userMail;
    FirebaseHandler fb = FirebaseHandler.getInstance();
    List<Order> orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_app);
        orders = new ArrayList<>();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        webView = findViewById(R.id.webView);

        WebAppInterface webAppInterface = new WebAppInterface(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(webAppInterface, "AndroidInterface");


        // Initialize ViewModel and retrieve user data
        userViewModel = new ViewModelProvider(this).get(userViewModel.class);
        observeUserData();

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

    public static class WebAppInterface {
        private final DriverApp mActivity;

        WebAppInterface(DriverApp activity) {
            this.mActivity = activity;
        }

        @android.webkit.JavascriptInterface
        public void onSignOutClicked() {
            FirebaseAuth.getInstance().signOut();
            mActivity.finish();
        }
    }



    private void loadInitialWebView() {
        // Load WebView with default content or any initial HTML before user data retrieval
        loadWebView("routs.html");
    }

    private void observeUserData() {
        userViewModel.getStudentFromVm().observe(this, student -> {
            if (student != null) {
                userName = student.getName();
                userMail = student.getEmail();
                loadWebView("profile.html");
            } else {
            }
        });
    }

    private void loadWebView(String fileName) {
        if(fileName.equals("profile.html")){
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    String mailInfo = userMail != null ? userMail : "";
                    String nameInfo = userName != null ? userName : "";

                    // Execute JavaScript function in the WebView after page is loaded
                    webView.evaluateJavascript(
                            "updateUserInfo('" + nameInfo + "', '" + mailInfo + "');",
                            null
                    );
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" + fileName);
        }
        else if(fileName.equals("requests.html")){
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    getOrders();
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" + fileName);
        }else if(fileName.equals("routs.html")){
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    getOrders();
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" + fileName);
        }else if(fileName.equals("rates.html")){
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    getOrders();
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" + fileName);
        }

    }

    void getOrders(){
        fb.getOrders(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        observeUserData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onSignOutClicked(View v) {
        FirebaseAuth.getInstance().signOut();
        if (this != null) {
            this.finish();
        }
    }

    @Override
    public void onOrdersReceived(List<Order> orders) {
        this.orders = orders;

        if(orders.isEmpty()){
            Log.e("NO ORDERS","NO ORDERS");
        }else{
            Log.e("Orders Found","Orders Found");
        }
        StringBuilder tableRows = new StringBuilder();

        for (Order order : orders) {
            tableRows.append("<tr>");
            tableRows.append("<td>").append(order.getUserID()).append("</td>");
            tableRows.append("<td>").append(order.getRideID()).append("</td>");
            tableRows.append("<td>").append(order.getTimeOfBooking()).append("</td>");
            tableRows.append("<td><button class=\"accept-button\">Accept</button>")
                    .append("<button class=\"decline-button\">Decline</button></td>");
            tableRows.append("</tr>");
        }

        // Inject JavaScript to add table rows to the tbody element
        webView.evaluateJavascript(
                "document.getElementById('ordersTableBody').innerHTML = '" + tableRows.toString() + "';",
                null
        );
    }

}
