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
        private TextView textDateTime;
        private TextView textSpecial;
        private TextView textService;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.textVisitRequestStatusClient);
            textDateTime = itemView.findViewById(R.id.textVisitRequestDateTimeClient);
            textService = itemView.findViewById(R.id.textVisitRequestServiceClient);
            textSpecial = itemView.findViewById(R.id.textVisitRequestSpecialClient);
        }

        void bindView(int position) {
            VisitRequest request = items.get(position);

            //TODO incorporate status and service provider
            textStatus.setText("Pending");
            textDateTime.setText(request.getDateTime());
            textService.setText(request.getService());
            textService.setText(request.getSpecial());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_visit_requests, parent, false);
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
