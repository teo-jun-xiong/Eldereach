package com.eldereach.eldereach.client.request;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.client.foodaid.FoodAidRequestsFragment;
import com.eldereach.eldereach.client.transport.TransportRequestsFragment;
import com.eldereach.eldereach.client.visit.VisitRequestsFragment;
import com.google.android.material.tabs.TabLayout;

/**
 * Contains a tab layout and an adapter to control the tabs
 */
public class RequestsClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_requests);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        RequestsClientTabAdapter adapter = new RequestsClientTabAdapter(getSupportFragmentManager());

        adapter.addFragment(new FoodAidRequestsFragment(), "Food Aid");
        adapter.addFragment(new TransportRequestsFragment(), "Transport");
        adapter.addFragment(new VisitRequestsFragment(), "Visit");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }
}
