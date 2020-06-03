package com.eldereach.eldereach.volunteer.request;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachRequest;
import com.eldereach.eldereach.util.FoodAidRequest;
import com.eldereach.eldereach.util.TransportRequest;
import com.eldereach.eldereach.util.VisitRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RequestsVolunteerActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RequestsVolunteerListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);

        initialiseComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    private void initialiseComponents() {
        db = FirebaseFirestore.getInstance();
        final RecyclerView recyclerView = findViewById(R.id.recyclerRequestVolunteer);
        final ArrayList<EldereachRequest> list = new ArrayList<>();

        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        db.collection("foodAidRequests").whereEqualTo("serviceProviderEmail", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : documentSnapshotList) {
                    list.add(new FoodAidRequest(d));
                }

                db.collection("transportRequests").whereEqualTo("serviceProviderEmail", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : documentSnapshotList) {
                            list.add(new TransportRequest(d));
                        }

                        db.collection("visitRequests").whereEqualTo("serviceProviderEmail", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                                for (DocumentSnapshot d : documentSnapshotList) {
                                    list.add(new VisitRequest(d));
                                }

                                listAdapter = new RequestsVolunteerListAdapter(sortDateStatus(list), RequestsVolunteerActivity.this);
                                recyclerView.setAdapter(listAdapter);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RequestsVolunteerActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    }
                });

            }
        });
    }

    private ArrayList<EldereachRequest> sortDateStatus(ArrayList<EldereachRequest> list) {
        Collections.sort(list, Comparator.comparing(EldereachRequest::getStatus)
                .thenComparing(EldereachRequest::getDateRequest)
                .thenComparing(EldereachRequest::getRequestType));

        return list;
    }
}
