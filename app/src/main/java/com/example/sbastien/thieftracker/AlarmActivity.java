package com.example.sbastien.thieftracker;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AlarmActivity extends Activity {

    private static final int GRACE_PERIOD = 10000;

    Button[] buttons;
    TextView pinField;
    String pin;
    Intent alarmIntent;
    private boolean alarmRunning;
    int[] backgroundColors;
    int currentBackgroundColor;
    private boolean passwordEntered;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        fullscreenAndroidStuff();

        alarmIntent = new Intent(this, AlarmService.class);
        alarmRunning = false;
        setupButtons();
        setupPasswordField();
        setupListeners();
        passwordEntered = false;

        startBlinking();
        startTimer();

    }

    private void startBlinking() {
        backgroundColors = new int[2];
        backgroundColors[0] = Color.RED;
        backgroundColors[1] = Color.BLUE;
        currentBackgroundColor = 0;
        blink();
    }

    private void startTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = GRACE_PERIOD;
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startAlarmSound();
            }
        }).start();
    }

    private void blink() {
        final Handler handler = new Handler();
        //currentBackgroundColor=(currentBackgroundColor+1)%3;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 200;
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        View alarmActivity = findViewById(R.id.alarmActivity);
                        alarmActivity.setBackgroundColor(backgroundColors[currentBackgroundColor]);
                        currentBackgroundColor = (currentBackgroundColor + 1) % 2;
                        blink();
                    }
                });
            }
        }).start();
    }

    private void fullscreenAndroidStuff() {
    }

    private void setupListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clicked = (Button) v;
                numberPressed(clicked.getText());
            }
        };
        for (Button b : buttons) {
            b.setOnClickListener(listener);
        }
    }

    private void setupPasswordField() {
        pinField = (TextView) findViewById(R.id.passwordField);
        pinField.setText("");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pin = preferences.getString(getString(R.string.pref_key_PIN), getString(R.string.pref_default_PIN));
    }

    private void numberPressed(CharSequence buttonText) {
        if (buttonText.toString().equals(getString(R.string.alarm_button_correct_text))) {
            CharSequence currentText = pinField.getText();
            pinField.setText(currentText.subSequence(0, currentText.length() - 1));
        } else {
            pinField.setText(String.format("%s%s", pinField.getText(), buttonText.toString()));
            if (pinField.length() == 4) {
                if (pinField.getText().toString().equals(pin)) {
                    stopAlarmSound();
                    finish();
                } else {
                    pinField.setText("");
                }
            }
        }
    }

    private void startAlarmSound() {

        /*alarmServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("AlarmActivity", "AlarmService connected");
                alarmService = ((AlarmService.LocalBinder) service).getService();
                alarmRunning = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                alarmService = null;
                alarmRunning = false;
            }
        };*/
        
        if (!alarmRunning && !passwordEntered) {
            startService(alarmIntent);
            alarmRunning = true;
            findViewById(R.id.alarmActivity).setBackgroundColor(Color.RED);
        }
    }

    private void stopAlarmSound() {
        stopService(alarmIntent);
        passwordEntered = true;
        alarmRunning = false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode != KeyEvent.KEYCODE_VOLUME_DOWN) && keyCode != KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
        }
        return true;
    }


    private void setupButtons() {
        buttons = new Button[11];
        buttons[0] = (Button) findViewById(R.id.button0);
        buttons[1] = (Button) findViewById(R.id.button1);
        buttons[2] = (Button) findViewById(R.id.button2);
        buttons[3] = (Button) findViewById(R.id.button3);
        buttons[4] = (Button) findViewById(R.id.button4);
        buttons[5] = (Button) findViewById(R.id.button5);
        buttons[6] = (Button) findViewById(R.id.button6);
        buttons[7] = (Button) findViewById(R.id.button7);
        buttons[8] = (Button) findViewById(R.id.button8);
        buttons[9] = (Button) findViewById(R.id.button9);
        buttons[10] = (Button) findViewById(R.id.buttonCorrect);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            //hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
