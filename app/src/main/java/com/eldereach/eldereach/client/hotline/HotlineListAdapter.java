package com.eldereach.eldereach.client.hotline;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachHotline;

import java.util.ArrayList;
import java.util.List;

public class HotlineListAdapter extends RecyclerView.Adapter {
    private ArrayList<EldereachHotline> items;
    private Context context;

    HotlineListAdapter(ArrayList<EldereachHotline> hotlines, Context context) {
        items = hotlines;
        this.context = context;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageLogo;
        private Dialog dialog;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLogo = itemView.findViewById(R.id.imageHotline);
        }

        void bindView(int position) {
            final EldereachHotline hotline = items.get(position);
            imageLogo.setImageResource(hotline.getImage());

            imageLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_hotline_client);
                    ImageView imagePopup = dialog.findViewById(R.id.imagePopup);
                    TextView textName = dialog.findViewById(R.id.textNamePopup);
                    TextView textDesc = dialog.findViewById(R.id.textDescriptionPopup);
                    TextView textHotline = dialog.findViewById(R.id.textHotlinePopup);
                    Button buttonCall = dialog.findViewById(R.id.buttonCall);

                    imagePopup.setImageResource(hotline.getImage());
                    textName.setText(hotline.getName());
                    textDesc.setText(hotline.getDescription());
                    textHotline.setText(hotline.getHotline());

                    buttonCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + hotline.getHotline()));
                            context.startActivity(callIntent);
                        }
                    });

                    dialog.show();
                }
            });
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
