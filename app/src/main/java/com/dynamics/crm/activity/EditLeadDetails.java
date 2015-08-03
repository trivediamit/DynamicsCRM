package com.dynamics.crm.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.utils.CommonUtils;
import com.dynamics.crm.utils.DatabaseUtil;

import java.util.ArrayList;

public class EditLeadDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText editTextTopic, editTextFirstName, editTextLastName, editTextCompanyName, editTextEmailId, editTextBusinessPhone, editTextOwner, editTextStatus;
    private String leadId;
    private ArrayList<String> leadValuesStrings;
    private DatabaseUtil databaseUtil;
    private TextView mTextViewLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_lead_details);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);

        mTextViewLead = (TextView) findViewById(R.id.lead_title);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readIntentData(getIntent());

        databaseUtil = new DatabaseUtil(this);

        leadValuesStrings = databaseUtil.getLeadDetails(leadId);

        mTextViewLead.setText(leadValuesStrings.get(1) + " " + leadValuesStrings.get(2));

        getEditTextView();

        setValueToEditText();
    }

    private void getEditTextView() {
        editTextTopic = (EditText) findViewById(R.id.edittext_topic);
        editTextFirstName = (EditText) findViewById(R.id.edittext_first_name);
        editTextLastName = (EditText) findViewById(R.id.edittext_last_name);
        editTextCompanyName = (EditText) findViewById(R.id.edittext_company_name);
        editTextEmailId = (EditText) findViewById(R.id.edittext_email_id);
        editTextBusinessPhone = (EditText) findViewById(R.id.edittext_business_phone);
        editTextOwner = (EditText) findViewById(R.id.edittext_owner);
        editTextStatus = (EditText) findViewById(R.id.edittext_status);
    }

    private void setValueToEditText() {
        editTextTopic.setText(leadValuesStrings.get(0));
        editTextFirstName.setText(leadValuesStrings.get(1));
        editTextLastName.setText(leadValuesStrings.get(2));
        editTextCompanyName.setText(leadValuesStrings.get(3));
        editTextEmailId.setText(leadValuesStrings.get(4));
        editTextBusinessPhone.setText(leadValuesStrings.get(5));
        editTextOwner.setText(leadValuesStrings.get(6));
        editTextStatus.setText(leadValuesStrings.get(7));
    }

    private void readIntentData(Intent intent) {
        if (intent.hasExtra("lead_id")) {
            leadId = intent.getStringExtra("lead_id");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_lead_details, menu);
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
        } else if (id == R.id.action_save) {
            saveData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cTopic", editTextTopic.getText().toString());
        contentValues.put("cFirstName", editTextFirstName.getText().toString());
        contentValues.put("cLastName", editTextLastName.getText().toString());
        contentValues.put("cCompanyName", editTextCompanyName.getText().toString());
        contentValues.put("cEmailId", editTextEmailId.getText().toString());
        contentValues.put("cBusinessPhone", editTextBusinessPhone.getText().toString());
        contentValues.put("cOwner", editTextOwner.getText().toString());
        contentValues.put("cStatus", editTextStatus.getText().toString());

        boolean isUpdated = databaseUtil.updateData(contentValues, leadId);
        if (isUpdated) {
            new CommonUtils(this).showToast("Saved");
        }
    }


}
