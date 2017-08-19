package com.app.dmitryteplyakov.sportdiary.Settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

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
            else if(sett.equals("graph"))
                addPreferencesFromResource(R.xml.preferences_graph);
            else if(sett.equals("timer"))
                addPreferencesFromResource(R.xml.preference_timer);
            else if(sett.equals("personal"))
                addPreferencesFromResource(R.xml.preference_personal);
        }
        else
            addPreferencesFromResource(R.xml.preferences);

    }
}