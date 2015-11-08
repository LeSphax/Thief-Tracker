package com.example.sbastien.thieftracker.FriendManager;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.sbastien.thieftracker.R;
import com.example.sbastien.thieftracker.model.Friends;
import com.example.sbastien.thieftracker.util.FriendListAdapter;

import java.util.List;


public class FriendsActivity extends AppCompatActivity {

    private List<Friends> friendsList;
    SharedPreference preference;
    FriendListAdapter adapter;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preference = new SharedPreference();
        friendsList = preference.getFriendsInfos(this);

        adapter = new FriendListAdapter(this, friendsList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        friendsList = preference.getFriendsInfos(this);

        adapter = new FriendListAdapter(this, friendsList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

}
