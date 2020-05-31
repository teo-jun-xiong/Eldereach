package com.eldereach.eldereach.volunteer.foodaid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachDateTime;
import com.eldereach.eldereach.util.FoodAidRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class FoodAidVolunteerActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeContainer;
    private FoodAidVolunteerRequestsListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_food_aid);

        initialiseComponents();
    }

    private void initialiseComponents() {
        db = FirebaseFirestore.getInstance();
        /*final RecyclerView recyclerView = findViewById(R.id.recyclerFoodAidVolunteer);
        swipeContainer = findViewById(R.id.foodAidSwipeContainerVolunteer);
        final ArrayList<FoodAidRequest> list = new ArrayList<>();

        db.collection("foodAidRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot q : Objects.requireNonNull(task.getResult())) {
                    list.add(new FoodAidRequest(q));
                }

                listAdapter = new FoodAidVolunteerRequestsListAdapter(sortDateTime(list));
                recyclerView.setAdapter(listAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FoodAidVolunteerActivity.this);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFoodAidRequests();
            }
        });

         */
    }

    private void refreshFoodAidRequests() {
        final ArrayList<FoodAidRequest> list = new ArrayList<>();

        db.collection("foodAidRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot q : Objects.requireNonNull(task.getResult())) {
                    list.add(new FoodAidRequest(q));
                }

                listAdapter.addAll(sortDateTime(list));
            }
        });

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    private ArrayList<FoodAidRequest> sortDateTime(ArrayList<FoodAidRequest> list) {
        Collections.sort(list, new Comparator<FoodAidRequest>() {
            @Override
            public int compare(FoodAidRequest request1, FoodAidRequest request2) {
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
