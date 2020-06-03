package com.eldereach.eldereach.volunteer.visit;

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
import com.eldereach.eldereach.util.VisitRequest;
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
public class VisitVolunteerRequestsListAdapter extends RecyclerView.Adapter {
    private ArrayList<VisitRequest> items;
    private Context context;

    public VisitVolunteerRequestsListAdapter(ArrayList<VisitRequest> visitRequests, Context context) {
        items = visitRequests;
        this.context = context;
    }

    // Inner class for a single RecyclerViewItem
    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textDate;
        private TextView textDateVisit;
        private TextView textAddress;
        private TextView textService;
        private TextView textSpecial;
        private ImageButton buttonView;
        private Dialog dialog;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textVisitRequestNameVolunteer);
            textDate = itemView.findViewById(R.id.textVisitRequestDateVolunteer);
            textDateVisit = itemView.findViewById(R.id.textVisitRequestDateTimeHomeVolunteer);
            textAddress = itemView.findViewById(R.id.textVisitRequestAddressHomeVolunteer);
            textService = itemView.findViewById(R.id.textVisitRequestServiceDestVolunteer);
            textSpecial = itemView.findViewById(R.id.textVisitRequestAddressDestVolunteer);
            buttonView = itemView.findViewById(R.id.buttonViewVisitVolunteer);
        }

        void bindView(int position) {
            final VisitRequest request = items.get(position);
            textName.setText("Name of client: " + request.getName());
            textDate.setText("Date of request: " + request.getDateRequest());
            textDateVisit.setText("Date of visit: " + request.getDateTime());
            textAddress.setText("Address of client: " + request.getAddress());
            textService.setText("Service required: " + request.getService());
            textSpecial.setText("Special notes: " + request.getSpecial());

            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_request_volunteer);

                    TextView textHeader = dialog.findViewById(R.id.textAcceptVolunteer);
                    ImageButton buttonAccept = dialog.findViewById(R.id.buttonTickVolunteer);
                    ImageButton buttonDecline = dialog.findViewById(R.id.buttonCrossVolunteer);

                    textHeader.setText("Accept this visit request?");
                    buttonAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // TODO code for accepting request, probably fetch the item and update the database
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final DocumentReference docRef = db.collection("visitRequests").document(request.getId());
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
                                            Toast.makeText(context, "Visit request accepted", Toast.LENGTH_SHORT).show();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_visit_volunteer_requests, parent, false);
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
