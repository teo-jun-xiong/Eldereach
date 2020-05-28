package com.eldereach.eldereach.client.hotline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.util.EldereachHotline;

import java.util.ArrayList;

public class HotlineClientActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_hotline);

        final RecyclerView recyclerView = findViewById(R.id.recyclerHotline);
        final ArrayList<EldereachHotline> list = EldereachHotline.getHotlines();

        HotlineListAdapter listAdapter = new HotlineListAdapter(list, HotlineClientActivity.this);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }
}
