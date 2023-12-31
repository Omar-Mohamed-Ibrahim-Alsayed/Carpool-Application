package com.example.carpool7813.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carpool7813.R;
import com.example.carpool7813.interfaces.OrdersCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.Adaptor;
import com.example.carpool7813.utilities.CartAdaptor;
import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends Fragment implements OrdersCallback {

    ProgressBar pb;
    CartAdaptor ordersAdapter;
    RecyclerView recycler;
    Button pay;
    FragmentManager fragmentManager;
    TextView total;
    FirebaseHandler fb;

    List<Order> orders;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        orders = new ArrayList<>();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        pb = view.findViewById(R.id.progressBar);
        recycler = view.findViewById(R.id.rview);
        pay = view.findViewById(R.id.payAll);
        total = view.findViewById(R.id.total);
        pb.setVisibility(View.INVISIBLE);
        fb = FirebaseHandler.getInstance();
        if (isAdded()) {
            fragmentManager = getParentFragmentManager();

        }



        ordersAdapter = new CartAdaptor(orders, getParentFragmentManager());
        getOrders();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager != null) {
                    List<Order> filteredOrders = new ArrayList<>();
                    for (Order order : orders) {
                        if (!order.getStatus().equals("pending")) {
                            filteredOrders.add(order);
                        }
                    }
                    fragmentManager.beginTransaction().replace(R.id.flFragment, new Payment(filteredOrders)).addToBackStack(null).commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onOrdersReceived(List<Order> orders) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (!order.getPaymentStatus().equals("paid")) {
                        filteredOrders.add(order);
            }
        }
        this.orders = filteredOrders;
        total.setText("Total = " + calculateTotal(orders));
        ordersAdapter.updateData(filteredOrders);
        recycler.setAdapter(ordersAdapter);
        pb.setVisibility(View.INVISIBLE);
    }

    private void getOrders() {
        pb.setVisibility(View.VISIBLE);
        String id = fb.getUserId();
        fb.getOrdersForUser(id,this);
    }

    private float calculateTotal(List<Order> orders) {
        float totalPrice = 0.0f;
        for (Order order : orders) {
            if(order.getStatus().equals("accepted") && !order.getPaymentStatus().equals("paid")){
            totalPrice += order.getPrice();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedPrice = decimalFormat.format(totalPrice);
        totalPrice = Float.parseFloat(formattedPrice);
        return totalPrice;
    }
}