package com.app.dmitryteplyakov.sportdiary.Settings;

import android.support.v4.app.Fragment;

import com.app.dmitryteplyakov.sportdiary.CommonActivity;
import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class SettingsActivity extends CommonActivity {

    @Override
    protected int getFragmentContainer() {
        return R.id.settings_fragment_container;
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }
}
