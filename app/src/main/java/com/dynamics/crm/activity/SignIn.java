package com.dynamics.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dynamics.crm.R;
import com.dynamics.crm.model.User;
import com.dynamics.crm.utils.CommonUtils;
import com.dynamics.crm.utils.DatabaseUtil;
import com.dynamics.crm.utils.PreferenceHandler;


public class SignIn extends AppCompatActivity implements View.OnClickListener {

    CommonUtils utils;
    DatabaseUtil databaseUtil;

    EditText editTextUsername, editTextPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        utils = new CommonUtils(this);
        databaseUtil = new DatabaseUtil(this);

        databaseUtil.insertDummyData();

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:

                editTextUsername = (EditText) findViewById(R.id.edtxt_username);
                editTextPassword = (EditText) findViewById(R.id.edtxt_password);

                if (isFieldsValid(editTextUsername, editTextPassword)) {
                    User user = databaseUtil.getUserDetails(editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim());
                    if (user != null) {

                        PreferenceHandler.writeBoolean(this, PreferenceHandler.USER_LOGIN_STATUS, true);

                        String username = "";
                        if (user.getUserName().contains("vibhor")) {
                            username = "Vibhor Mehta";
                        } else {
                            username = "Admin";
                        }

                        PreferenceHandler.writeString(this, PreferenceHandler.USERNAME, username);
                        PreferenceHandler.writeString(this, PreferenceHandler.USER_EMAIL, editTextUsername.getText().toString());

                        Intent intent;
                        if ("0".equalsIgnoreCase(user.getIsAdmin())) {
                            PreferenceHandler.writeBoolean(this, PreferenceHandler.IS_ADMIN, false);

                            intent = new Intent(SignIn.this, UserDashboard.class);
                            startActivity(intent);
                        } else {
                            PreferenceHandler.writeBoolean(this, PreferenceHandler.IS_ADMIN, true);

                            intent = new Intent(SignIn.this, AdminDashboard.class);
                            startActivity(intent);
                        }
                        this.finish();
                    } else {
                        utils.showToast(getString(R.string.error_invalid_error));
                    }
                }
                break;

            default:
                break;
        }

    }

    private boolean isFieldsValid(final EditText editText1, final EditText editText2) {

        if (utils.isEmpty(editText1)) {
            utils.showToast(getString(R.string.error_username_empty));
            return false;
        } else if (utils.isEmpty(editText2)) {
            utils.showToast(getString(R.string.error_password_empty));
            return false;
        } else {
            return true;
        }
    }
}
