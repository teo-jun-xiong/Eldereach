package com.eldereach.eldereach.client.visit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachDateTime;
import com.eldereach.eldereach.util.VisitRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VisitRequestsFragment extends Fragment {
    private String email;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeContainer;
    private VisitRequestsListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_requests_client, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        db = FirebaseFirestore.getInstance();
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerVisitFragment);
        swipeContainer = view.findViewById(R.id.visitSwipeContainer);
        final ArrayList<VisitRequest> list = new ArrayList<>();

        CollectionReference collectionReference = db.collection("visitRequests");
        collectionReference.whereEqualTo("email", email)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : documentSnapshotList) {
                    list.add(new VisitRequest(d));
                }

                listAdapter = new VisitRequestsListAdapter(sortDateTime(list));
                recyclerView.setAdapter(listAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshVisitRequests();
            }
        });

        return view;
    }

    private void refreshVisitRequests() {
        final ArrayList<VisitRequest> list = new ArrayList<>();

        CollectionReference collectionReference = db.collection("visitRequests");
        collectionReference.whereEqualTo("email", email)
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
