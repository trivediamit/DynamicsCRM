package com.dynamics.crm.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.adapters.LeadDetailsAdapter;
import com.dynamics.crm.adapters.LocationHistoryAdapter;
import com.dynamics.crm.model.LeadLocation;
import com.dynamics.crm.utils.CommonUtils;
import com.dynamics.crm.utils.DatabaseUtil;

import java.util.ArrayList;

public class LocationHistory extends AppCompatActivity implements LocationHistoryAdapter.ItemClickListener {

    private String leadId;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerViewHistory;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<LeadLocation> leadLocationArrayList;
    private DatabaseUtil databaseUtil;
    private String leadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData(getIntent());

        TextView leadTitle = (TextView) findViewById(R.id.lead_title);
        leadTitle.setText(leadName);

        databaseUtil = new DatabaseUtil(this);

        leadLocationArrayList = databaseUtil.getLeadLocationHistory(leadId);

        mRecyclerViewHistory = (RecyclerView) findViewById(R.id.recycler_view_location_history);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerViewHistory.setLayoutManager(mLayoutManager);

        mHistoryAdapter = new LocationHistoryAdapter(this, leadLocationArrayList);
        mRecyclerViewHistory.setAdapter(mHistoryAdapter);
        ((LocationHistoryAdapter) mHistoryAdapter).setClickListener(this);


    }

    private void getIntentData(Intent intent) {
        if (intent.hasExtra("lead_id")) {
            leadId = intent.getStringExtra("lead_id");
        }
        if (intent.hasExtra("lead_name")) {
            leadName = intent.getStringExtra("lead_name");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(View view, int position) {
        new CommonUtils(this).showToast("Location clicked!");
    }
}
