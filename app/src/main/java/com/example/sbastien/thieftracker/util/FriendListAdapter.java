package com.example.sbastien.thieftracker.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sbastien.thieftracker.R;
import com.example.sbastien.thieftracker.model.Friends;

import java.util.List;


public class FriendListAdapter extends ArrayAdapter<Friends>{

    private List <Friends> items;
    int layoutResource;
    Context context;

    public FriendListAdapter(Context context, int layoutResource, List<Friends> items){
        super(context,layoutResource,items);
        this.items = items;
        this.layoutResource = layoutResource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FriendsHolder holder = new FriendsHolder();

        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(layoutResource, parent, false);

        holder.friend = items.get(position);
        holder.deleteButton = (ImageButton) row.findViewById(R.id.buttonFriends);
        holder.deleteButton.setTag(holder.friend);

        holder.friendName = (TextView) row.findViewById(R.id.nameField);

        holder.friendPhone = (TextView) row.findViewById(R.id.phoneField);

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
        ImageButton deleteButton;
    }

}
