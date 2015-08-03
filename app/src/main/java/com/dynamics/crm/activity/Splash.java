package com.dynamics.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dynamics.crm.R;
import com.dynamics.crm.utils.PreferenceHandler;


public class Splash extends AppCompatActivity {

    //region Variables

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                // Check if user is already logged in
                boolean isLoggedIn = PreferenceHandler.readBoolean(Splash.this, PreferenceHandler.USER_LOGIN_STATUS, false);
                if (isLoggedIn) {

                    boolean isAdmin = PreferenceHandler.readBoolean(Splash.this, PreferenceHandler.IS_ADMIN, false);
                    if (isAdmin) {
                        // Go to Admin Dashboard
                        Intent intent = new Intent(Splash.this, AdminDashboard.class);
                        startActivity(intent);
                    } else {
                        // Go to User Dashboard
                        Intent intent = new Intent(Splash.this, UserDashboard.class);
                        startActivity(intent);
                    }
                } else {
                    // Go to Sign In screen
                    Intent i = new Intent(Splash.this, SignIn.class);
                    startActivity(i);
                }
                // close this activity
                Splash.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
