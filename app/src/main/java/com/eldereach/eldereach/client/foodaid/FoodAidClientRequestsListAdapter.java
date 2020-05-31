package com.eldereach.eldereach.client.foodaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.util.FoodAidRequest;
import com.eldereach.eldereach.R;

import java.util.ArrayList;
import java.util.List;

/** Adapter to control the items inside the RecyclerView of FoodAidRequestsFragment */
public class FoodAidClientRequestsListAdapter extends RecyclerView.Adapter {
    private ArrayList<FoodAidRequest> items;

    public FoodAidClientRequestsListAdapter(ArrayList<FoodAidRequest> foodAidRequests) {
        items = foodAidRequests;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textStatus;
        private TextView textDateTime;
        private TextView textMeals;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.textFoodAidRequestStatusClient);
            textDateTime = itemView.findViewById(R.id.textFoodAidRequestDateTimeClient);
            textMeals = itemView.findViewById(R.id.textFoodAidRequestMealsClient);
        }

        void bindView(int position) {
            FoodAidRequest request = items.get(position);

            //TODO incorporate status and service provider
            textStatus.setText("Pending");
            textDateTime.setText(request.getDateTime());
            textMeals.setText(request.getMeals());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_aid_client_requests, parent, false);
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
    public void addAll(List<FoodAidRequest> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}
