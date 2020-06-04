package com.eldereach.eldereach.client.visit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.VisitRequest;

import java.util.ArrayList;
import java.util.List;

class VisitRequestsListAdapter extends RecyclerView.Adapter {
    private ArrayList<VisitRequest> items;

    public VisitRequestsListAdapter(ArrayList<VisitRequest> visitRequests) {
        items = visitRequests;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textStatus;
        private TextView textDateTimeRequest;
        private TextView textDateTime;
        private TextView textService;
        private TextView textSpecial;
        private TextView textServiceProvider;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.textVisitRequestStatusClient);
            textDateTimeRequest = itemView.findViewById(R.id.textVisitRequestDateClient);
            textDateTime = itemView.findViewById(R.id.textVisitRequestDateTimeHomeClient);
            textService = itemView.findViewById(R.id.textVisitRequestServiceDestClient);
            textSpecial = itemView.findViewById(R.id.textVisitRequestSpecialClient);
            textServiceProvider = itemView.findViewById(R.id.textVisitRequestServiceProviderDetailsClient);
        }

        void bindView(int position) {
            VisitRequest request = items.get(position);

            //TODO incorporate status and service provider
            textStatus.setText("Status: " + request.getStatus());
            textDateTimeRequest.setText("Date of request: " + request.getDateRequest());
            textDateTime.setText("Date of visit: " + request.getDateTime());
            textService.setText("Serivce required: " + request.getService());
            textSpecial.setText("Special requirements: " + request.getSpecial());
            textServiceProvider.setText("Service provider: " + request.getServiceProviderName() + " (" + request.getServiceProviderPhone() + ")");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_visit_client_requests, parent, false);
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
    public void addAll(List<VisitRequest> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}
