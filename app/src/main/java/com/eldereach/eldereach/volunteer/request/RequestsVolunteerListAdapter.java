package com.eldereach.eldereach.volunteer.request;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachRequest;
import com.eldereach.eldereach.util.FoodAidRequest;
import com.eldereach.eldereach.util.TransportRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

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
        private Dialog dialog;
        private RelativeLayout rl;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textRequestNameVolunteer);
            dateRequest = itemView.findViewById(R.id.textRequestDateVolunteer);
            textRequestType = itemView.findViewById(R.id.textRequestTypeVolunteer);
            textStatus = itemView.findViewById(R.id.textStatusVolunteer);
            buttonView = itemView.findViewById(R.id.buttonViewVolunteer);
            rl = itemView.findViewById(R.id.layoutRequest);
        }

        void bindView(int position) {
            final EldereachRequest request = items.get(position);
            textName.setText("Name of client: " + request.getName());
            textRequestType.setText("Request type: " + request.getRequestType());
            dateRequest.setText("Date of request: " + request.getDateRequest());
            textStatus.setText("Status: " + request.getStatus());

            if (request.getStatus().equals("Completed")) {
                rl.setAlpha((float) 0.5);

                buttonView.setVisibility(View.INVISIBLE);
            }

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setAlpha((float) 1);

                    new Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    view.setAlpha((float) 0.5);
                                }
                            },
                            3000);
                }
            });

            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_complete_request_volunteer);

                    ImageButton buttonAccept = dialog.findViewById(R.id.buttonCompleteTickVolunteer);
                    ImageButton buttonDecline = dialog.findViewById(R.id.buttonCompleteCrossVolunteer);

                    buttonAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();

                            String typeOfRequest = "";

                            if (request instanceof FoodAidRequest) {
                                typeOfRequest = "foodAidRequests";
                            } else if (request instanceof TransportRequest) {
                                typeOfRequest = "transportRequests";
                            } else {
                                typeOfRequest = "visitRequests";
                            }

                            final DocumentReference docRef = db.collection(typeOfRequest).document(request.getId());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    final Map<String, Object> request = documentSnapshot.getData();
                                    request.put("status", 2);
                                    Toast.makeText(context, "Request accepted", Toast.LENGTH_SHORT).show();
                                    docRef.set(request);
                                    dialog.dismiss();
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
