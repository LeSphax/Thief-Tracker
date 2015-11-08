package com.example.sbastien.thieftracker.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sbastien.thieftracker.FriendManager.NewFriendActivity;
import com.example.sbastien.thieftracker.R;
import com.example.sbastien.thieftracker.FriendManager.SharedPreference;
import com.example.sbastien.thieftracker.model.Friends;

import java.util.List;


public class FriendListAdapter extends ArrayAdapter<Friends>{

    public final static String EXTRA_MESSAGE = "EDIT";
    private List <Friends> items;
    private Context context;
    private SharedPreference preference;

    public FriendListAdapter(Context context, List<Friends> items){
        super(context, R.layout.item_friend_layout, items);
        this.items = items;
        this.context = context;
        this.preference = new SharedPreference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final FriendsHolder holder = new FriendsHolder();

        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.item_friend_layout, parent, false);

        holder.friend = items.get(position);


        holder.friendName = (TextView) row.findViewById(R.id.nameField);

        holder.friendPhone = (TextView) row.findViewById(R.id.phoneField);
        holder.editButton = (ImageButton) row.findViewById(R.id.buttonEdit);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = context.getSharedPreferences("FRIEND",0);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("NAME", items.get(position).getName());
                edit.putString("PHONE", items.get(position).getPhoneNumber());
                System.out.println("NAME " + items.get(position).getName());
                edit.commit();
                Intent intent = new Intent(context, NewFriendActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "edit" );
                context.startActivity(intent);
            }
        });

        holder.deleteButton = (ImageButton) row.findViewById(R.id.buttonDelete);
        holder.deleteButton.setTag(holder.friend);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog(items.get(position)).show();
               //remove(items.get(position));
            }
        });

        row.setTag(holder);

        setUpItem(holder);
        return row;
    }

    public void setUpItem(FriendsHolder holder) {
        holder.friendName.setText(holder.friend.getName());
        holder.friendPhone.setText(holder.friend.getPhoneNumber());
    }

    public static class FriendsHolder {
        Friends friend;
        TextView friendName;
        TextView friendPhone;
        ImageButton editButton;
        ImageButton deleteButton;
    }

    @Override
    public void add(Friends friends){
        super.add(friends);
        preference.addFriend(context, friends);

    }

    @Override
    public void remove(Friends friends){
        super.remove(friends);
        preference.removeFriend(context, friends);
    }

    private AlertDialog confirmDialog(final Friends friend) {
        AlertDialog alert = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Do you really want to delete " + friend.getName())
                //.setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(friend);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return alert;
    }
}
