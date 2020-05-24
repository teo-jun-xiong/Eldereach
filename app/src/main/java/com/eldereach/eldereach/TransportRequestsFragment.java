package com.eldereach.eldereach;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

// In this case, the fragment displays simple text based on the page
public class TransportRequestsFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeContainer;
    private TransportRequestsListAdapter listAdapter;
    private ArrayList<String> dateTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transport_requests_client, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        final ArrayList<TransportRequest> list = new ArrayList<>();

        /*


         */

        CollectionReference collectionReference = db.collection("transportRequests");
        collectionReference.whereEqualTo("email", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : documentSnapshotList) {
                    list.add(new TransportRequest(d));
                }

                listAdapter = new TransportRequestsListAdapter(sortDateTime(list));
                recyclerView.setAdapter(listAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshTransportRequests(0);
            }
        });

        return view;
    }

    public void refreshTransportRequests(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        final ArrayList<TransportRequest> list = new ArrayList<>();

        CollectionReference collectionReference = db.collection("transportRequests");
        collectionReference.whereEqualTo("email", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : documentSnapshotList) {
                    list.add(new TransportRequest(d));
                }


                listAdapter.addAll(sortDateTime(list));
            }
        });

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    private ArrayList<TransportRequest> sortDateTime(ArrayList<TransportRequest> list) {
        Collections.sort(list, new Comparator<TransportRequest>() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH);
            @Override
            public int compare(TransportRequest request1, TransportRequest request2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(request1.getHomeDate())).compareTo(simpleDateFormat.parse(request2.getHomeDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return list;
    }
}
