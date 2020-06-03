package com.eldereach.eldereach.volunteer.visit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachDateTime;
import com.eldereach.eldereach.util.VisitRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VisitVolunteerActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeContainer;
    private VisitVolunteerRequestsListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_visit);

        initialiseComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    private void initialiseComponents() {
        db = FirebaseFirestore.getInstance();
        final RecyclerView recyclerView = findViewById(R.id.recyclerVisitVolunteer);
        swipeContainer = findViewById(R.id.visitSwipeContainerVolunteer);
        final ArrayList<VisitRequest> list = new ArrayList<>();

        db.collection("visitRequests").whereEqualTo("status", 0)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : documentSnapshotList) {
                    list.add(new VisitRequest(d));
                }

                System.out.println("size" + list.size());

                listAdapter = new VisitVolunteerRequestsListAdapter(sortDateTime(list), VisitVolunteerActivity.this);
                recyclerView.setAdapter(listAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VisitVolunteerActivity.this);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFoodAidRequests();
            }
        });
    }

    private void refreshFoodAidRequests() {
        final ArrayList<VisitRequest> list = new ArrayList<>();


        db.collection("visitRequests").whereEqualTo("status", 0)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : documentSnapshotList) {
                    list.add(new VisitRequest(d));
                }

                listAdapter.addAll(sortDateTime(list));
            }
        });

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    private ArrayList<VisitRequest> sortDateTime(ArrayList<VisitRequest> list) {
        Collections.sort(list, new Comparator<VisitRequest>() {
            @Override
            public int compare(VisitRequest request1, VisitRequest request2) {
                try {
                    return Objects.requireNonNull(EldereachDateTime.simpleDateFormat.parse(request1.getDateTime())).compareTo(EldereachDateTime.simpleDateFormat.parse(request2.getDateTime()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        return list;
    }
}
