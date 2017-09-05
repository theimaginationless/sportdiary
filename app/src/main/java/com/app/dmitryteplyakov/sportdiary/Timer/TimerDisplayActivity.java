package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 23.08.17.
 */

public class TimerDisplayActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.common_fragment_container);
        if(fragment == null) {
            fragment = new TimerDisplayFragment();
            fm.beginTransaction().
                    add(R.id.common_fragment_container, fragment)
                    .commit();
        }
    }
}
