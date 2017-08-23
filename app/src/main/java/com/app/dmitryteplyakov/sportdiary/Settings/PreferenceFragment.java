package com.app.dmitryteplyakov.sportdiary.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 16.08.17.
 */

public class PreferenceFragment extends PreferenceFragmentCompat {
    private static final String ARG_PREF = "com.app.preferenceFragment.arg_pref";
    public static PreferenceFragment newInstance(String sett) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PREF, sett);
        PreferenceFragment fragment = new PreferenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        if(getArguments().getSerializable(ARG_PREF) != null) {
            String sett = (String) getArguments().getSerializable(ARG_PREF);
            if(sett.equals("general"))
                addPreferencesFromResource(R.xml.preferences_general);
            else if(sett.equals("graph")) {
                addPreferencesFromResource(R.xml.preferences_graph);

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String mode = sp.getString("graph_mode_list", getString(R.string.combined));
                ListPreference pref = (ListPreference) findPreference("graph_mode_list");
                final ListPreference prefColor = (ListPreference) findPreference("graph_color_list_multi");
                final boolean state = sp.getBoolean("switch_on_graphs", true);
                if(mode.equals(getString(R.string.combined)))
                    prefColor.setEnabled(false);

                pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        final String mode = newValue.toString();
                        if(!mode.equals(getString(R.string.combined)) && state) {
                            prefColor.setEnabled(true);
                        } else
                            prefColor.setEnabled(false);
                        return true;
                    }
                });
            }
            else if(sett.equals("timer"))
                addPreferencesFromResource(R.xml.preference_timer);
            else if(sett.equals("personal"))
                addPreferencesFromResource(R.xml.preference_personal);
        }
        else
            addPreferencesFromResource(R.xml.preferences);

    }
}