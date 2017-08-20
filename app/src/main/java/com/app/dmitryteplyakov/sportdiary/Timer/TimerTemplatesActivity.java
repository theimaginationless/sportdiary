package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerTemplatesActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Todo: Timer
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
