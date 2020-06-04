package com.eldereach.eldereach.client.foodaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.FoodAidRequest;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

/** Adapter to control the items inside the RecyclerView of FoodAidRequestsFragment */
public class FoodAidClientRequestsListAdapter extends Adapter {
    private ArrayList<FoodAidRequest> items;

    public FoodAidClientRequestsListAdapter(ArrayList<FoodAidRequest> foodAidRequests) {
        items = foodAidRequests;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends ViewHolder {
        private TextView textStatus;
        private TextView textDateRequest;
        private TextView textDateDelivery;
        private TextView textMeals;
        private TextView textDietary;
        private TextView textServiceProvider;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.textFoodAidRequestStatusClient);
            textDateRequest = itemView.findViewById(R.id.textFoodAidRequestDateTimeRequestClient);
            textDateDelivery = itemView.findViewById(R.id.textFoodAidRequestDateTimeClient);
            textMeals = itemView.findViewById(R.id.textFoodAidRequestTypeClient);
            textDietary = itemView.findViewById(R.id.textFoodAidRequestDietaryClient);
            textServiceProvider = itemView.findViewById(R.id.textFoodAidRequestServiceProviderDetailsClient);
        }

        void bindView(int position) {
            FoodAidRequest request = items.get(position);

            textStatus.setText("Status: " + request.getStatus());
            textDateRequest.setText("Date of request: " + request.getDateRequest());
            textDateDelivery.setText("Date of delivery: " + request.getDateTime());
            textMeals.setText("Types of food aid: "  + request.getMeals());
            textDietary.setText("Dietary restrictions: " + request.getDietary());
            textServiceProvider.setText("Service provider: " + request.getServiceProviderName() + " (" + request.getServiceProviderPhone() + ")");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_aid_client_requests, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Replaces the list of items with another one - to be used for refreshing the view
    public void addAll(List<FoodAidRequest> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}
