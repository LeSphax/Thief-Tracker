package com.example.sbastien.thieftracker.FriendManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sbastien.thieftracker.R;
import com.example.sbastien.thieftracker.model.Friends;
import com.example.sbastien.thieftracker.util.FriendListAdapter;

public class NewFriendActivity extends AppCompatActivity {

    Friends friend;
    SharedPreference preference;
    private AppCompatEditText name;
    private AppCompatEditText phoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        preference = new SharedPreference();
        Intent intent = getIntent();
        if (intent.getStringExtra(FriendListAdapter.EXTRA_MESSAGE) != null) {
            viewAdapter();
        }

    }

    public void onCancelClick(View view) {
        finish();
    }

    public void onRegisterClick(View view) {
        name = (AppCompatEditText) findViewById(R.id.nameField);
        phoneNumber = (AppCompatEditText) findViewById(R.id.phoneField);
        friend = new Friends(name.getText().toString(), phoneNumber.getText().toString());
        if (isPossibleRegister(name, phoneNumber)) {
            Intent intent = getIntent();
            if(intent.getStringExtra(FriendListAdapter.EXTRA_MESSAGE) != null){
                preference.removeFriend(this, getOldFriend());
                preference.addFriend(this,friend);
            }else{
                preference.addFriend(this, friend);
            }
            finish();
        }
    }

    private boolean isPossibleRegister(AppCompatEditText name, AppCompatEditText phoneNumber) {
        LinearLayout view = (LinearLayout) findViewById(R.id.contentView);

        if (name.getText().toString().equals("")) {
             Snackbar.make(view, "You need to enter a friend name before register", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (phoneNumber.getText().toString().equals("")) {
            Snackbar.make(view, "You need to enter a phone number before register", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (!phoneNumber.getText().toString().matches("(0|\\\\+33|0033)[1-9][0-9]{8}")){
            Snackbar.make(view, "The phone number must be valid", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        return true;
    }

    private void viewAdapter() {
        String labelName;
        String labelPhone;
        TextView textView = (TextView) findViewById(R.id.viewName);
        name = (AppCompatEditText) findViewById(R.id.nameField);
        phoneNumber = (AppCompatEditText) findViewById(R.id.phoneField);

        SharedPreferences pref = getSharedPreferences("FRIEND",0);
        friend = new Friends(labelName = pref.getString("NAME",""), labelPhone = pref.getString("PHONE", ""));

        name.setText(labelName);
        phoneNumber.setText(labelPhone);
        textView.setText("Edit your friend informations");
    }

    private Friends getOldFriend() {
        Friends oldFriend;
        SharedPreferences pref = getSharedPreferences("FRIEND",0);
        oldFriend = new Friends(pref.getString("NAME",""), pref.getString("PHONE", ""));

        return oldFriend;
    }
}
