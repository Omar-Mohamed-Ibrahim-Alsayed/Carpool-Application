package com.example.carpool7813.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpool7813.R;
import com.example.carpool7813.fragments.Orderfrag;

import java.util.List;
public class Adaptor extends RecyclerView.Adapter<Adaptor.RoutViewHolder> {
    private List<Rout> routs;
    private FragmentManager fragmentManager;
    private boolean isGrid;

    public Adaptor(List<Rout> routs, FragmentManager fragmentManager, boolean isGrid) {
        this.routs = routs;
        this.fragmentManager = fragmentManager;
        this.isGrid = isGrid;
    }
    public void updateData(List<Rout> newRouts) {
        this.routs.clear();
        this.routs.addAll(newRouts);
        notifyDataSetChanged();
    }

    @Override
    public RoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRoutView;
        if (isGrid) {
            itemRoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination, parent, false);
        } else {
            itemRoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_single, parent, false);
        }
        return new RoutViewHolder(itemRoutView);
    }

    @Override
    public void onBindViewHolder(RoutViewHolder holder, int position) {
        Rout current = routs.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return routs.size();
    }
    public void updateLayout(boolean isGrid) {
        this.isGrid = isGrid;
        notifyDataSetChanged(); // Notify adapter about dataset change
    }

    public class RoutViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView describtion;
        private ImageView image;

        public RoutViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.distName);
            image = itemView.findViewById(R.id.distImage);
            describtion = itemView.findViewById(R.id.distInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int layoutPosition = getLayoutPosition();
                    Rout current = routs.get(layoutPosition);
                    fragmentManager.beginTransaction().replace(R.id.flFragment, new Orderfrag(current)).addToBackStack(null).commit();
                }
            });
        }

        public void bind(Rout Rout) {
            name.setText(Rout.getStartLocation() + '/' + Rout.getDestination());
            describtion.setText(Rout.getFormattedDepartureTime().toString() + "\n" + "Available Seats: "+Rout.getSeatsAvailable());
        }
    }
}
