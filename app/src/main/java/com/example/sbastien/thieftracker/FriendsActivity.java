package com.example.sbastien.thieftracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sbastien.thieftracker.model.Friends;
import com.example.sbastien.thieftracker.util.FriendListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsActivity extends AppCompatActivity {


    FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final List<Friends> numberList = new ArrayList<>();
        Friends one = new Friends("pops","03585647858");
        //numberList.add(one);

        adapter = new FriendListAdapter(this, R.layout.item_friend_layout, numberList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Friends one = new Friends("pops","03585647858");
                adapter.add(one);
            }
        });
    }

    public void removeFriend(View view) {
        Friends item = (Friends) view.getTag();
        adapter.remove(item);
    }


}
