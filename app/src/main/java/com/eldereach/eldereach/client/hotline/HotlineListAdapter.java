package com.eldereach.eldereach.client.hotline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachHotline;

import java.util.ArrayList;
import java.util.List;

public class HotlineListAdapter extends RecyclerView.Adapter {
    private ArrayList<EldereachHotline> items;

    public HotlineListAdapter(ArrayList<EldereachHotline> hotlines) {
        items = hotlines;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageLogo;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLogo = itemView.findViewById(R.id.imageHotline);
        }

        void bindView(int position) {
            EldereachHotline hotline = items.get(position);

            //TODO incorporate status and service provider
            imageLogo.setImageResource(hotline.getImage());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hotline_client, parent, false);
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
    public void addAll(List<EldereachHotline> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}
