package com.eldereach.eldereach.volunteer.foodaid;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.TransportRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Requests shown by the list adapter shall only be of status "pending".
public class TransportVolunteerRequestsListAdapter extends RecyclerView.Adapter {
    private ArrayList<TransportRequest> items;
    private Context context;

    public TransportVolunteerRequestsListAdapter(ArrayList<TransportRequest> TransportRequest, Context context) {
        items = TransportRequest;
        this.context = context;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textDateRequest;
        private TextView textDateHome;
        private TextView textDateDest;
        private TextView textHomeAddress;
        private TextView textDestAddress;
        private TextView textPurpose;
        private TextView textDietary;
        private ImageButton buttonView;
        private Dialog dialog;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textTransportRequestNameVolunteer);
            textDateRequest = itemView.findViewById(R.id.textTransportRequestDateVolunteer);
            textDateHome = itemView.findViewById(R.id.textTransportRequestDateTimeHomeVolunteer);
            textDateDest = itemView.findViewById(R.id.textTransportRequestDateTimeDestVolunteer);
            textHomeAddress = itemView.findViewById(R.id.textTransportRequestAddressHomeVolunteer);
            textDestAddress = itemView.findViewById(R.id.textTransportRequestAddressDestVolunteer);
            textPurpose = itemView.findViewById(R.id.textTransportRequestPurposeVolunteer);
            buttonView = itemView.findViewById(R.id.buttonViewTransportVolunteer);
        }

        void bindView(int position) {
            final TransportRequest request = items.get(position);
            textName.setText("Name of client: " + request.getName());
            textDateRequest.setText("Date of request: " + request.getDateRequest());
            textDateHome.setText("Date of pickup from home: " + request.getDateTime());
            textDateDest.setText("Date of pickup from destination: " + request.getDestDate());
            textHomeAddress.setText("Address of client: " + request.getHomeAddress());
            textDestAddress.setText("Address of destination: " + request.getDestAddress());
            textPurpose.setText("Purpose of transport: " + request.getPurpose());

            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_request_volunteer);

                    TextView textAccept = dialog.findViewById(R.id.textAcceptVolunteer);
                    ImageButton buttonAccept = dialog.findViewById(R.id.buttonTickVolunteer);
                    ImageButton buttonDecline = dialog.findViewById(R.id.buttonCrossVolunteer);
                    textAccept.setText("Accept this transport request?");

                    buttonAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // TODO code for accepting request, probably fetch the item and update the database
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final DocumentReference docRef = db.collection("transportRequests").document(request.getId());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    final Map<String, Object> request = documentSnapshot.getData();

                                    final String[] userInfo = {"", ""};
                                    db.collection("users").document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Map<String, Object> map = documentSnapshot.getData();
                                            userInfo[0] = (String) map.get("name");
                                            userInfo[1] = (String) map.get("phone");
                                            request.put("serviceProviderName", userInfo[0]);
                                            request.put("serviceProviderPhone", userInfo[1]);
                                            request.put("status", 1);
                                            docRef.set(request);
                                            Toast.makeText(context, "Transport request accepted", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    });

                    buttonDecline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transport_volunteer_requests, parent, false);
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
    public void addAll(List<TransportRequest> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}
