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
import com.dynamics.crm.adapters.LeadsListAdapter;
import com.dynamics.crm.model.Lead;
import com.dynamics.crm.utils.CommonUtils;
import com.dynamics.crm.utils.DatabaseUtil;

import java.util.ArrayList;

public class LeadList extends AppCompatActivity implements LeadsListAdapter.ItemClickListener {

    private Toolbar mToolbar;
    private TextView mSalesPersonTitle;

    private DatabaseUtil databaseUtil;

    private ArrayList<Lead> leadArrayList;

    private RecyclerView mRecyclerViewLeads;
    private RecyclerView.Adapter mLeadsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String salesPersonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_list);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData(getIntent());

        mSalesPersonTitle = (TextView) findViewById(R.id.sales_person_title);
        mSalesPersonTitle.setText(salesPersonName);

        databaseUtil = new DatabaseUtil(this);
        leadArrayList = databaseUtil.getLeadsList();

        mRecyclerViewLeads = (RecyclerView) findViewById(R.id.recycler_view_leads);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewLeads.setLayoutManager(mLayoutManager);

        mLeadsAdapter = new LeadsListAdapter(this, leadArrayList);
        ((LeadsListAdapter) mLeadsAdapter).setClickListener(this);
        mRecyclerViewLeads.setAdapter(mLeadsAdapter);
    }

    private void getIntentData(Intent intent) {
        if (intent.hasExtra("salesPersonName")) {
            salesPersonName = intent.getStringExtra("salesPersonName");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lead_list, menu);
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
        Intent intent = new Intent(this, LocationHistory.class);
        intent.putExtra("lead_id", leadArrayList.get(position).getId());
        intent.putExtra("lead_name", leadArrayList.get(position).getFirstName() + " " + leadArrayList.get(position).getLastName());
        startActivity(intent);
    }
}
