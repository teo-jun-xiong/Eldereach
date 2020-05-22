package com.eldereach.eldereach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransportRequestsListAdapter extends RecyclerView.Adapter {
    ArrayList<String> arr;

    public TransportRequestsListAdapter(ArrayList<String> strings) {
        arr = strings;
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
        return arr.size();
    }

    public void addItems(String str) {
        arr.add(str);
        notifyItemInserted(arr.size() - 1);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bindView(int position) {
            textView.setText(arr.get(position));
        }
    }
}
