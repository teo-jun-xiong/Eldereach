package com.eldereach.eldereach;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * Contains a tab layout and an adapter to control the tabs
 */
public class RequestsClientActivity extends AppCompatActivity {
    private RequestsClientTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_requests);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new RequestsClientTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransportRequestsFragment(), "Tab 1");
        adapter.addFragment(new TransportRequestsFragment(), "Tab 2");
        adapter.addFragment(new TransportRequestsFragment(), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
