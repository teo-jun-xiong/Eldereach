package com.eldereach.eldereach.client.hotline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachHotline;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class HotlineListAdapter extends RecyclerView.Adapter {
    private ArrayList<EldereachHotline> items;
    private Context context;
    private LinearLayout linearLayout;

    public HotlineListAdapter(ArrayList<EldereachHotline> hotlines, Context context, LinearLayout linearLayout) {
        items = hotlines;
        this.context = context;
        this.linearLayout = linearLayout;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageLogo;
        private PopupWindow popupWindow;

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
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_window, null);
                    ImageView imagePopup = popupView.findViewById(R.id.imagePopup);
                    TextView textName = popupView.findViewById(R.id.textNamePopup);
                    TextView textDesc = popupView.findViewById(R.id.textDescriptionPopup);
                    TextView textHotline = popupView.findViewById(R.id.textHotlinePopup);
                    popupWindow = new PopupWindow(view, WRAP_CONTENT, WRAP_CONTENT);

                    imagePopup.setImageResource(hotline.getImage());
                    textName.setText(hotline.getName());
                    textDesc.setText(hotline.getDescription());
                    textHotline.setText(hotline.getHotline());

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            //Close the window when clicked
                            popupWindow.dismiss();
                            return true;
                        }
                    });

                    System.out.print("1\n2\n3\n4\n");
                    //popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
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
