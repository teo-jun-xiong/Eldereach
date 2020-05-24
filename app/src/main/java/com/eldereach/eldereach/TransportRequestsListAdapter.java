package com.eldereach.eldereach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Adapter to control the items inside the RecyclerView of TransportsRequestsFragment */
public class TransportRequestsListAdapter extends RecyclerView.Adapter {
    private ArrayList<TransportRequest> items;

    TransportRequestsListAdapter(ArrayList<TransportRequest> transportRequests) {
        items = transportRequests;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textCategory;
        private TextView textDateTimeHome;
        private TextView textDateTimeDest;
        private TextView textLocation;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textTransportRequestCategoryClient);
            textDateTimeHome = itemView.findViewById(R.id.textTransportRequestDateTimeHomeClient);
            textDateTimeDest = itemView.findViewById(R.id.textTransportRequestDateTimeDestClient);
            textLocation = itemView.findViewById(R.id.textTransportRequestLocationClient);
        }

        void bindView(int position) {
            TransportRequest request = items.get(position);

            textCategory.setText("Transport");
            textDateTimeHome.setText(request.getHomeDate());

            if (request.getReturnNeeded()) {
                textDateTimeDest.setText(request.getDestDate());
            } else {
                textDateTimeDest.setText("Return trip not required");
            }

            textLocation.setText(request.getLocation());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transport_requests, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Replaces the list of items with another one - to be used for refreshing the view
    public void addAll(List<TransportRequest> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}