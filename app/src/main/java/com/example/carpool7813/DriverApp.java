package com.example.carpool7813;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.carpool7813.ViewModel.userViewModel;
import com.example.carpool7813.interfaces.OrdersCallback;
import com.example.carpool7813.interfaces.RoutsCallback;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.model.userProfile;
import com.example.carpool7813.utilities.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DriverApp extends AppCompatActivity implements OrdersCallback, UserCallback {
    BottomNavigationView bottomNavigationView;
    WebView webView;
    userViewModel userViewModel;
    String userName,userMail = null;
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
        openProfile();

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

    @Override
    public void onCallback(LiveData<userProfile> user) {
        String fileName = "profile.html";
        if (user != null) {
            user.observeForever(userProfile -> {
                if (userProfile != null) {
                    userName = userProfile.getName();
                    userMail = userProfile.getEmail();

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
                    webView.getSettings().setDomStorageEnabled(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("file:///android_asset/" + fileName);
                }else{
                    userViewModel.getStudentFromVm(this);
                }
            });
        }
        else{
            userViewModel.getStudentFromVm(this);
        }

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
    private void openProfile(){
        loadWebView("loading.html");
        if(userName == null || userMail == null){
        userViewModel.getStudentFromVm(this);
        }else{
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
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" + "profile.html");
        }
    }
    private void loadWebView(String fileName) {
        if(fileName.equals("profile.html")){
            openProfile();
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
        }else if(fileName.equals("loading.html")){

            webView.loadUrl("file:///android_asset/" + fileName);
        }

    }

    void getOrders(){
        fb.getOrders(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //openProfile();
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
        userViewModel.deleteAll();
    }

    @Override
    public void onOrdersReceived(List<Order> orders) {
        this.orders = orders;

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
