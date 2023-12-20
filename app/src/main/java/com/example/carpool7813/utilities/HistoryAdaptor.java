package com.example.carpool7813.utilities;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpool7813.R;

import java.text.DecimalFormat;
import java.util.List;

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.OrderViewModel> {
    private List<Order> Orders;
    private FragmentManager fragmentManager;
    private boolean isGrid;

    public HistoryAdaptor(List<Order> Orders, FragmentManager fragmentManager) {
        this.Orders = Orders;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public HistoryAdaptor.OrderViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRoutView;
        itemRoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new HistoryAdaptor.OrderViewModel(itemRoutView);
    }

    public void updateData(List<Order> orders) {
        this.Orders.clear(); // Clear existing data
        this.Orders.addAll(orders); // Add new data
        notifyDataSetChanged(); // Notify adapter about dataset change
    }

    @Override
    public void onBindViewHolder(HistoryAdaptor.OrderViewModel holder, int position) {
        Order current = Orders.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }
    public void updateLayout(boolean isGrid) {
        this.isGrid = isGrid;
        notifyDataSetChanged(); // Notify adapter about dataset change
    }

    public class OrderViewModel extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView status;

        public OrderViewModel(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.destinationName);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);

        }

        public void bind(Order order) {
            name.setText(order.getOrderID());
            status.setText(order.getStatus());
            date.setText(order.getTimeOfBooking().toString() );
        }
    }
}

