package com.dynamics.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dynamics.crm.R;
import com.dynamics.crm.adapters.SalesPersonListAdapter;
import com.dynamics.crm.model.SalesPerson;
import com.dynamics.crm.utils.DatabaseUtil;
import com.dynamics.crm.utils.PreferenceHandler;

import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity implements SalesPersonListAdapter.ItemClickListener {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerViewSalesPerson;
    private RecyclerView.Adapter mSalesPersonAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseUtil databaseUtil;

    ArrayList<SalesPerson> salesPersonArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mRecyclerViewSalesPerson = (RecyclerView) findViewById(R.id.recycler_view_sales_person);

        databaseUtil = new DatabaseUtil(this);
        salesPersonArrayList = databaseUtil.getSalesPersonList();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewSalesPerson.setLayoutManager(mLayoutManager);

        mSalesPersonAdapter = new SalesPersonListAdapter(this, salesPersonArrayList);
        ((SalesPersonListAdapter) mSalesPersonAdapter).setClickListener(this);
        mRecyclerViewSalesPerson.setAdapter(mSalesPersonAdapter);

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
        Intent intent = new Intent(this, LeadList.class);
        intent.putExtra("salesPersonName",salesPersonArrayList.get(position).getName());
        startActivity(intent);
    }
}