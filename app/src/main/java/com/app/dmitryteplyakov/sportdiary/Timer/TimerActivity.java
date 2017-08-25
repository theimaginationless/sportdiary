package com.app.dmitryteplyakov.sportdiary.Timer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private static final String EXTRA_TIMER_UUID = "com.app.timeractivity.extra_timer_uuid";

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra(EXTRA_TIMER_UUID, uuid);
        return intent;
    }

    public Fragment createFragment() {
        return TimerFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_TIMER_UUID));
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.common_fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.common_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
