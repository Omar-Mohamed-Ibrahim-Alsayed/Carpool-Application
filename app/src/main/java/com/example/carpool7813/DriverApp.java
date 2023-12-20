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
import com.example.carpool7813.fragments.Routs;
import com.example.carpool7813.interfaces.OrdersCallback;
import com.example.carpool7813.interfaces.RoutsCallback;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.model.userProfile;
import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverApp extends AppCompatActivity implements OrdersCallback, UserCallback, RoutsCallback {
    BottomNavigationView bottomNavigationView;
    WebView webView;
    static userViewModel userViewModel;
    String userName,userMail = null;
    FirebaseHandler fb = FirebaseHandler.getInstance();
    List<Order> orders;
    List<Rout> routs;
    LocalTime morningRideCutoffTime = LocalTime.of(23, 30);
    LocalTime afternoonRideCutoffTime = LocalTime.of(16, 30);
    LocalDateTime rideDateTime;
    static Rout newRout;

    @Override
    protected void onStart() {
        super.onStart();
        fb.getRouts(this);
        userViewModel.getStudentFromVm(this);

    }

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
    public void onCallback(LiveData<userProfile> userLiveData) {

    }

    @Override
    public void onSpecialCall(userProfile userProfile) {
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
        } else {
            //userViewModel.getStudentFromVm(this);
        }
    }

    public static class WebAppInterface {
        private final DriverApp mActivity;

        WebAppInterface(DriverApp activity) {
            this.mActivity = activity;
        }

        @android.webkit.JavascriptInterface
        public void handleData(String tripData) {

            mActivity.handleDataFromWebView(tripData);
        }

        @android.webkit.JavascriptInterface
        public void handleOrderReply(String orderID, boolean accept) {

            Log.e("DAte","INNNNNNNNNNNNNNN HANDLEREPLY");
            mActivity.handleReply(orderID, accept);
        }

        @android.webkit.JavascriptInterface
        public void onSignOutClicked() {
            userViewModel.deleteAll();
            FirebaseAuth.getInstance().signOut();
            mActivity.finish();
        }

        @android.webkit.JavascriptInterface
        public void updateNow() {
            mActivity.update();
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

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" +fileName);
        }
        else if(fileName.equals("requests.html")){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("file:///android_asset/" + fileName);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    getOrders();
                    super.onPageFinished(view, url);
                }
            });
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

        List<Order> filteredOrders = new ArrayList<>();
        List<Rout> filteredRouts = new ArrayList<>();
        if (routs != null && !routs.isEmpty()) {
            String currentUserDriverId = fb.getUserId();

            for (Rout rout : routs) {
                if (rout.getDriverID().equals(currentUserDriverId)) {
                    for (Order order : orders) {
                        if (order.getRideID().equals(rout.getRideId())) {
                            filteredOrders.add(order);
                            filteredRouts.add(rout);
                        }
                    }
                }
            }
        }else{
            filteredOrders = orders;
        }


        routs = filteredRouts;
        StringBuilder tableRows = new StringBuilder();
        LocalDateTime currentDateTime = LocalDateTime.now();


        JSONArray jsonArray = new JSONArray();

        for (Rout rout : routs) {
            for (Order order : filteredOrders) {
                if (order.getRideID().equals(rout.getRideId())) {
                    rideDateTime = rout.getDepartureTime();
                    LocalDateTime morningRideCutoff = LocalDateTime.of(rideDateTime.minusDays(1).toLocalDate(),morningRideCutoffTime);
                    LocalDateTime afternoonRideCutoff = LocalDateTime.of(rideDateTime.minusDays(1).toLocalDate(),morningRideCutoffTime);


                    if ((rideDateTime.toLocalTime().equals(LocalTime.of(7, 30)) &&
                            currentDateTime.isAfter(morningRideCutoff))||(rideDateTime.toLocalTime().equals(LocalTime.of(7, 30)) &&currentDateTime.toLocalTime().isBefore(LocalTime.of(7, 30)))) {
                        JSONObject orderJson = new JSONObject();
                        try {
                            orderJson.put("userID", order.getUserID());
                            orderJson.put("destination", rout.getDestination()); // Replace getOrderDestination with your logic
                            orderJson.put("timeOfBooking", order.getTimeOfBooking());
                            orderJson.put("status","passed");
                            orderJson.put("orderID", order.getOrderID());
                            jsonArray.put(orderJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // Check for afternoon ride cut-off
                    else if (rideDateTime.toLocalTime().equals(LocalTime.of(17, 30)) &&
                            currentDateTime.isAfter(afternoonRideCutoff)) {
                        JSONObject orderJson = new JSONObject();
                        try {
                            orderJson.put("userID", order.getUserID());
                            orderJson.put("destination", rout.getDestination()); // Replace getOrderDestination with your logic
                            orderJson.put("timeOfBooking", order.getTimeOfBooking());
                            orderJson.put("status","passed");
                            orderJson.put("orderID", order.getOrderID());
                            jsonArray.put(orderJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        JSONObject orderJson = new JSONObject();
                        try {
                            orderJson.put("userID", order.getUserID());
                            orderJson.put("destination", rout.getDestination()); // Replace getOrderDestination with your logic
                            orderJson.put("timeOfBooking", order.getTimeOfBooking());
                            orderJson.put("status", order.getStatus());
                            orderJson.put("orderID", order.getOrderID());
                            jsonArray.put(orderJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

        String jsonOrders = jsonArray.toString();

        // Pass the JSON string to the WebView
        webView.evaluateJavascript("updateOrders('" + jsonOrders + "');", null);
    }

    @Override
    public void onRoutsReceived(List<Rout> routs) {
        this.routs = routs;
    }

    public void handleDataFromWebView(String tripData) {
        try {
            JSONObject jsonObject = new JSONObject(tripData);
            Log.e("DAte","formattedDateTime");

            String startLocation = jsonObject.optString("startLocation");
            String endLocation = jsonObject.optString("endLocation");
            String seats = jsonObject.optString("seats");
            String tripDate = jsonObject.optString("tripDate");
            String tripTime = jsonObject.optString("tripTime");
            String uid = fb.getUserId();

            LocalDate date = LocalDate.parse(tripDate);
            LocalTime time = LocalTime.parse(tripTime.toUpperCase(), DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH));

            LocalDateTime dateTime = LocalDateTime.of(date, time);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMMM/yyyy-HH:mm", Locale.ENGLISH);
            String formattedDateTime = dateTime.format(formatter);




            fb.addRout(this,"",startLocation,endLocation,formattedDateTime,formattedDateTime,Integer.parseInt(seats),uid,"Active");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("DAte","ERROR");

        }catch (Exception e){
            Log.e("DAte","ERROR");
        }
    }

    void handleReply(String orderID, boolean accept){
        Log.e("DAte","INNNNNNNNNNNNNNN HANDLEREPLY");
        fb.reply(orderID,accept);
    }

    void update(){
        loadWebView("requests.html");
    }
}
