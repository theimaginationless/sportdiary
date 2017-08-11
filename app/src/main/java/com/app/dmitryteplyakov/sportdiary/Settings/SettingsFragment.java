package com.app.dmitryteplyakov.sportdiary.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        return v;
    }

    private class SettingsHolder extends RecyclerView.ViewHolder {
        public SettingsHolder(View itemView) {
            super(itemView);
        }
    }
}
