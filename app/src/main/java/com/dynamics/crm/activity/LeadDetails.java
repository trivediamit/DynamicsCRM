package com.dynamics.crm.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamics.crm.R;
import com.dynamics.crm.adapters.LeadDetailsAdapter;
import com.dynamics.crm.dialogs.AttachmentDialogFragment;
import com.dynamics.crm.utils.CommonUtils;
import com.dynamics.crm.utils.DatabaseUtil;
import com.dynamics.crm.utils.GPSTracker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LeadDetails extends AppCompatActivity implements AttachmentDialogFragment.Communicator, View.OnClickListener {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerViewDetails;
    private RecyclerView.Adapter mLeadDetailsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mLeadTitleTextView;
    private LinearLayout attachmentLayout;

    DatabaseUtil databaseUtil;
    CommonUtils commonUtils;

    ArrayList<String> leadValuesStrings;
    private String leadId;
    private String attachmentPath;

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int SIGNATURE_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mLeadTitleTextView = (TextView) findViewById(R.id.lead_title);
        attachmentLayout = (LinearLayout) findViewById(R.id.attachment_layout);
        attachmentLayout.setOnClickListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readIntentData(getIntent());

        commonUtils = new CommonUtils(this);

        databaseUtil = new DatabaseUtil(this);
        leadValuesStrings = databaseUtil.getLeadDetails(leadId);

        mLeadTitleTextView.setText(leadValuesStrings.get(1) + " " + leadValuesStrings.get(2));
        mLeadTitleTextView.setTypeface(Typeface.DEFAULT_BOLD);

        mRecyclerViewDetails = (RecyclerView) findViewById(R.id.recycler_view_lead_details);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerViewDetails.setLayoutManager(mLayoutManager);

        mLeadDetailsAdapter = new LeadDetailsAdapter(this, leadValuesStrings);
        mRecyclerViewDetails.setAdapter(mLeadDetailsAdapter);

        trackLocation();
    }

    private void trackLocation() {
        GPSTracker gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // store the location in database
            String leadName = leadValuesStrings.get(1) + " " + leadValuesStrings.get(2);
            databaseUtil.storeLocation(leadId, leadName, String.valueOf(latitude), String.valueOf(longitude));
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private void readIntentData(Intent intent) {
        if (intent.hasExtra("lead_id")) {
            leadId = intent.getStringExtra("lead_id");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lead_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_attachment) {
            AttachmentDialogFragment dialog = new AttachmentDialogFragment();
            dialog.setCommunicator(this);
            dialog.show(getSupportFragmentManager(), "AttachmentDialogFragment");
            return true;
        } else if (id == R.id.action_edit) {
            // call the edit screen
            Intent intent = new Intent(this, EditLeadDetails.class);
            intent.putExtra("lead_id", leadId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            new CommonUtils(this).showToast("Saved");

            // store the image attachment for the current lead in attachments table
            databaseUtil.storeAttachment(leadId, attachmentPath, "Photo");
        } else if (requestCode == SIGNATURE_ACTIVITY && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String status = bundle.getString("status");
            if (status.equalsIgnoreCase("done")) {
                new CommonUtils(this).showToast("Signature capture successful!");

                attachmentPath = bundle.getString("attachmentPath");

                // store the signature attachment for the current lead in attachments table
                databaseUtil.storeAttachment(leadId, attachmentPath, "Signature");
            }
        }
    }

    @Override
    public void callback(String action) {
        if ("camera".equalsIgnoreCase(action)) {
            callCamera();
        } else if ("signature".equalsIgnoreCase(action)) {
            callSignature();
        }
    }

    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = commonUtils.createImageFile();
                attachmentPath = photoFile.getAbsolutePath();
                Log.i("File path: ", attachmentPath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                takePictureIntent.putExtra("filePath", photoFile.getAbsolutePath());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void callSignature() {
        Intent intent = new Intent(this, CaptureSignature.class);
        startActivityForResult(intent, SIGNATURE_ACTIVITY);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.attachment_layout) {
            Intent intent = new Intent(this, Attachments.class);
            intent.putExtra("lead_id", leadId);
            startActivity(intent);
        }
    }
}
