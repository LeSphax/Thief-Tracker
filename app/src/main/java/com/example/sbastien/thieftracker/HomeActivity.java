package com.example.sbastien.thieftracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sbastien.thieftracker.FriendManager.FriendsActivity;
import com.example.sbastien.thieftracker.FriendManager.SharedPreference;

public class HomeActivity extends AppCompatActivity {

    private static final int ADMIN_INTENT = 15;
    private static final String description = "Some Description About Your Admin";
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    //private SmsReceiver smsReceiver;
    private SharedPreference preferences;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
       // smsReceiver = new SmsReceiver();
        preferences = new SharedPreference();
        activity = this;


        Button buttonFriends = (Button) findViewById(R.id.buttonFriends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        ToggleButton buttonMovement = (ToggleButton) findViewById(R.id.buttonMovement);
        buttonMovement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToggleButton button = (ToggleButton) v;
                if (button.isChecked()) {
                    Intent myIntent = new Intent(getApplicationContext(), SensorActivity.class);
                    startActivityForResult(myIntent, 0);
                }
            }
        });

        ToggleButton buttonUnplugg = (ToggleButton) findViewById(R.id.buttonUnplugged);
        buttonUnplugg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToggleButton button = (ToggleButton) v;
                if (button.isChecked()) {
                    preferences.setLoaderPlugState(activity,true);
                }
            }
        });
        boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
        if (!isAdmin)
            createPopUp();
    }

    private void createPopUp() {
        new AlertDialog.Builder(this)
                .setTitle("Setup Admin Rights")
                .setMessage("This application needs administrator rights to function correctly, please give this application authorization to lock your phone.")
                .setPositiveButton("Enable admin rights", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HomeActivity.this.startActivityForResult(new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)

                                .putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName)
                                .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description), ADMIN_INTENT)
                        ;
                    }
                })
                .setNegativeButton("Quit Application", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HomeActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else {
            Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(myIntent, 0);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }

        if (resultCode == 111) {
            ToggleButton buttonUnPlugg = (ToggleButton) findViewById(R.id.buttonUnplugged);
            buttonUnPlugg.setChecked(false);
        }


    }




}
