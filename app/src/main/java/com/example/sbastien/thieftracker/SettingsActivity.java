package com.example.sbastien.thieftracker;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    static boolean onMainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        onMainPage = true;
        setView(new SecurityParametersPreferenceFragment());

    }

    private void setView(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                fragment).commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || AboutFragment.class.getName().equals(fragmentName)
                || SecurityParametersPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onBackPressed() {
        if (!onMainPage) {
            setView(new SecurityParametersPreferenceFragment());
            onMainPage = true;
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SecurityParametersPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        private SeekBarPreference _seekBarSensitivity;
        private DevicePolicyManager devicePolicyManager;
        private ComponentName demoDeviceAdmin;

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_security_settings);
            setHasOptionsMenu(true);

            _seekBarSensitivity = (SeekBarPreference) this.findPreference(this.getString(R.string.pref_sensitivity_key));
            initPreferences();

            // Set listener :
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        private void setView(Fragment fragment) {
            getFragmentManager().beginTransaction().replace(android.R.id.content,
                    fragment).commit();
        }


        private void initPreferences() {

            Preference commandsButton = findPreference(getString(R.string.pref_key_commands));
            commandsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    setView(new CommandsFragment());
                    SettingsActivity.onMainPage = false;
                    return true;
                }
            });


            Preference aboutButton = findPreference(getString(R.string.pref_key_about));
            aboutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    setView(new AboutFragment());
                    SettingsActivity.onMainPage = false;
                    return true;
                }
            });

            setSensitivityBarSummary();
            bindPreferenceSummaryToValue(findPreference("PIN"));
        }

        private void setSensitivityBarSummary() {
            int radius = PreferenceManager.getDefaultSharedPreferences(this.getActivity())
                    .getInt(this.getString(R.string.pref_sensitivity_key), Integer.parseInt(getString(R.string.pref_sensitivity_defaulValue)));
            _seekBarSensitivity.setSummary(this.getString(R.string.pref_sensitivity_summary).replace("$1", "" + radius));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals(getString(R.string.pref_key_PIN))) {


                //Making sure the PIN has 4 digits
                if (sharedPreferences.getString(getString(R.string.pref_key_PIN), "0").length() != 4) {
                    Toast.makeText(getActivity(), "The PIN should be 4 digits", Toast.LENGTH_LONG).show();
                    EditTextPreference pinPreference = (EditTextPreference) findPreference(getString(R.string.pref_key_PIN));
                    pinPreference.setText(null);
                    pinPreference.setSummary("The PIN should be 4 digits");
                    return;
                } else {
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                }
            }
            else if (key.equals(getString(R.string.pref_sensitivity_key)))
                setSensitivityBarSummary();
        }
    }

}
