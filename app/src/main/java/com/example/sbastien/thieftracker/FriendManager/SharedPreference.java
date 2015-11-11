package com.example.sbastien.thieftracker.FriendManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.sbastien.thieftracker.model.Friends;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "INFOS";
    public static final String FRIENDS_INFOS = "Friends_Infos";

    public SharedPreference() {
        super();
    }

    public ArrayList<Friends> getFriendsInfos(Context context) {
        SharedPreferences preferences;
        List<Friends> friendsList;

        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(FRIENDS_INFOS)) {
            String jsonFriends = preferences.getString(FRIENDS_INFOS, null);
            Gson gson = new Gson();
            Friends[] friendsInfos = gson.fromJson(jsonFriends, Friends[].class);

            friendsList = Arrays.asList(friendsInfos);
            friendsList = new ArrayList<>(friendsList);
        }else
            return new ArrayList<>();
        return (ArrayList<Friends>) friendsList;
    }

    public void saveFriendsInfos(Context context, List<Friends> friendsList) {
        SharedPreferences preferences;
        Editor editor;

        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        Gson gson = new Gson();
        String friendsInfos = gson.toJson(friendsList);

        editor.putString(FRIENDS_INFOS, friendsInfos);
        editor.commit();
    }

    public void addFriend(Context context, Friends friends) {
        List<Friends> friendList = getFriendsInfos(context);
        if (friendList == null) {
            friendList = new ArrayList<>();
        }
        friendList.add(friends);
        saveFriendsInfos(context, friendList);
    }

    public void removeFriend(Context context, Friends friends) {
        List<Friends> friendList = getFriendsInfos(context);
        if(friendList != null)
            friendList.remove(friends);
        saveFriendsInfos(context, friendList);
    }

    public void setLoaderPlugState(Context context, boolean bool) {
        SharedPreferences preferences;
        Editor editor;

        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean("PLUG", bool);
        editor.commit();
    }

    public boolean getloaderPlugState(Context context) {
        SharedPreferences preferences;

        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("PLUG", false);
    }

    public boolean contains(Context context, String phoneNumber) {
        List<Friends> friendList = getFriendsInfos(context);
        String myFriendPhone;
        for (Friends friend: friendList) {
            myFriendPhone = friend.getPhoneNumber();
            if (myFriendPhone.equals(phoneNumber) || myFriendPhone.replaceFirst(("\\+33"), "0").equals(phoneNumber.replaceFirst("(\\+33)","0"))
                    || myFriendPhone.replaceFirst(("0033"),"0").equals(phoneNumber.replaceFirst("(\\+33)","0")))
                return true;
        }
        return false;
    }
}
