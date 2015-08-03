package com.dynamics.crm.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.adapters.LeadsListAdapter;
import com.dynamics.crm.model.Lead;
import com.dynamics.crm.utils.CommonUtils;
import com.dynamics.crm.utils.DatabaseUtil;
import com.dynamics.crm.utils.PreferenceHandler;

import java.util.ArrayList;

public class UserDashboard extends BaseActivity implements LeadsListAdapter.ItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView mRecyclerViewLeads;
    private RecyclerView.Adapter mLeadsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseUtil databaseUtil;

    ArrayList<Lead> leadArrayList;

    private TextView mUsername, mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setupToolbar();

        setupNavigationDrawer();

        setNavigationDrawerList();

        databaseUtil = new DatabaseUtil(this);

        mUsername = (TextView) findViewById(R.id.username_title);
        mUsername.setText(PreferenceHandler.getPreferences(this).getString(PreferenceHandler.USERNAME, ""));

        mUserEmail = (TextView) findViewById(R.id.username_email);
        mUserEmail.setText(PreferenceHandler.getPreferences(this).getString(PreferenceHandler.USER_EMAIL, ""));

        mRecyclerViewLeads = (RecyclerView) findViewById(R.id.recycler_view_leads);

        leadArrayList = databaseUtil.getLeadsList();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewLeads.setLayoutManager(mLayoutManager);

        mLeadsAdapter = new LeadsListAdapter(this, leadArrayList);
        ((LeadsListAdapter) mLeadsAdapter).setClickListener(this);
        mRecyclerViewLeads.setAdapter(mLeadsAdapter);
    }

    private void setNavigationDrawerList() {
        String[] navigationItems = getResources().getStringArray(R.array.navigation_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.navigation_list);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, navigationItems));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Leads
                        break;
                    case 1:
                        // Appointments
                        break;
                    case 2:
                        // Abouts us
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            PreferenceHandler.writeBoolean(this, PreferenceHandler.USER_LOGIN_STATUS, false);
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(View view, int position) {
        String leadId = leadArrayList.get(position).getId();

        Intent intent = new Intent(this, LeadDetails.class);
        intent.putExtra("lead_id", leadId);
        startActivity(intent);
    }
}
