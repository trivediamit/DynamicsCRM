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
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.adapters.AttachmentListAdapter;
import com.dynamics.crm.model.Attachment;
import com.dynamics.crm.utils.DatabaseUtil;

import java.util.ArrayList;

public class Attachments extends AppCompatActivity {

    private String leadId;

    private Toolbar mToolbar;
    private RecyclerView recyclerViewAttachments;
    private RecyclerView.LayoutManager mLayoutManager;
    private AttachmentListAdapter mAttachmentListAdapter;

    private DatabaseUtil databaseUtil;

    ArrayList<Attachment> attachmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        recyclerViewAttachments = (RecyclerView) findViewById(R.id.recycler_view_attachments);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readIntentData(getIntent());

        databaseUtil = new DatabaseUtil(this);
        attachmentArrayList = databaseUtil.getAttachments(leadId);

        mLayoutManager = new LinearLayoutManager(this);

        recyclerViewAttachments.setLayoutManager(mLayoutManager);

        mAttachmentListAdapter = new AttachmentListAdapter(this, attachmentArrayList);
        recyclerViewAttachments.setAdapter(mAttachmentListAdapter);
    }

    private void readIntentData(Intent intent) {
        if (intent.hasExtra("lead_id")) {
            leadId = intent.getStringExtra("lead_id");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attachments, menu);
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
}
