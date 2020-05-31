package com.eldereach.eldereach.volunteer.foodaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.FoodAidRequest;

import java.util.ArrayList;
import java.util.List;

// Requests shown by the list adapter shall only be of status "pending".
public class FoodAidVolunteerRequestsListAdapter  extends RecyclerView.Adapter {
    private ArrayList<FoodAidRequest> items;

    public FoodAidVolunteerRequestsListAdapter(ArrayList<FoodAidRequest> foodAidRequests) {
        items = foodAidRequests;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textDate;
        private TextView textDateDelivery;
        private TextView textAddress;
        private TextView textType;
        private TextView textDietary;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textFoodAidRequestNameVolunteer);
            textDate = itemView.findViewById(R.id.textFoodAidRequestDateVolunteer);
            textDateDelivery = itemView.findViewById(R.id.textFoodAidRequestDateTimeVolunteer);
            textAddress = itemView.findViewById(R.id.textFoodAidRequestAddressVolunteer);
            textType = itemView.findViewById(R.id.textFoodAidRequestTypeVolunteer);
            textDietary = itemView.findViewById(R.id.textFoodAidRequestDietaryVolunteer);
        }

        void bindView(int position) {
            FoodAidRequest request = items.get(position);


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
