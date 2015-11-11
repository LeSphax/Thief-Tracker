package com.example.sbastien.thieftracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.sbastien.thieftracker.FriendManager.SharedPreference;
import com.example.sbastien.thieftracker.model.Friends;

import java.util.List;
import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver{
    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";
    private final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
    private static final String PREFS_NAME = "INFOS";
    SharedPreference preference;
    SharedPreferences pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        List<Friends> friendsList;
        preference = new SharedPreference();
        friendsList = preference.getFriendsInfos(context);
        pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if(intent.getAction().equals(ACTION_RECEIVE_SMS)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                final String phoneNumber = messages[0].getDisplayOriginatingAddress();
                final String messageBody = messages[0].getMessageBody();

                if(preference.contains(context, phoneNumber)) {
                    if(messages.length > -1) {

                    }
                }
            }
        }

        if (intent.getAction().equals(ACTION_POWER_DISCONNECTED) && pref.getBoolean("PLUG", false)) {
            Toast.makeText(context,"OKKKKKKK", Toast.LENGTH_LONG);
            Intent myIntent = new Intent();
            myIntent.setClassName("com.example.sbastien.thieftracker",
                    "com.example.sbastien.thieftracker.AlarmActivity");
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }

}
