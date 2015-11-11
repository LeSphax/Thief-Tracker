package com.example.sbastien.thieftracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CommandsFragment extends Fragment {



    public static CommandsFragment newInstance() {
        CommandsFragment fragment = new CommandsFragment();
        return fragment;
    }

    public CommandsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pref_commands, container, false);
    }




}
