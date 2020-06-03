package com.eldereach.eldereach.volunteer.request;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachRequest;

import java.util.ArrayList;

public class RequestsVolunteerListAdapter extends RecyclerView.Adapter {
    private ArrayList<EldereachRequest> items;
    private Context context;

    public RequestsVolunteerListAdapter(ArrayList<EldereachRequest> requests, Context context) {
        items = requests;
        this.context = context;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView dateRequest;
        private TextView textRequestType;
        private TextView textStatus;
        private ImageButton buttonView;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textRequestNameVolunteer);
            dateRequest = itemView.findViewById(R.id.textRequestDateVolunteer);
            textRequestType = itemView.findViewById(R.id.textRequestTypeVolunteer);
            textStatus = itemView.findViewById(R.id.textStatusVolunteer);
            buttonView = itemView.findViewById(R.id.buttonViewVolunteer);
        }

        void bindView(int position) {
            final EldereachRequest request = items.get(position);
            textName.setText("Name of client: " + request.getName());
            textRequestType.setText("Request type: " + request.getRequestType());
            dateRequest.setText("Date of request: " + request.getDateRequest());
            textStatus.setText("Status: " + request.getStatus());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_request_volunteer, parent, false);
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
}
