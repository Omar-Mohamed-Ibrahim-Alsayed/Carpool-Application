package com.example.carpool7813.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpool7813.R;

import java.util.List;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.OrderViewModel> {
    private List<Order> Orders;
    private FragmentManager fragmentManager;
    private boolean isGrid;

    public CartAdaptor(List<Order> Orders, FragmentManager fragmentManager) {
        this.Orders = Orders;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public CartAdaptor.OrderViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRoutView;
        itemRoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartAdaptor.OrderViewModel(itemRoutView);
    }

    @Override
    public void onBindViewHolder(CartAdaptor.OrderViewModel holder, int position) {
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
        private TextView price;
        private ImageView image;

        public OrderViewModel(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.destinationName);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int layoutPosition = getLayoutPosition();
//                    //Order current = Orders.get(layoutPosition);
//                    //fragmentManager.beginTransaction().replace(R.id.flFragment, new Order(current)).addToBackStack(null).commit();
//                }
//            });
        }

        public void bind(Order order) {
            name.setText(order.getOrderID());
            date.setText(order.getTimeOfBooking().toString() );
            price.setText(String.valueOf(order.getPrice()) + " EGP");
        }
    }
}

