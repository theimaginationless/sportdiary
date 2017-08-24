package com.app.dmitryteplyakov.sportdiary.Timer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerListActivity extends AppCompatActivity {
    private static final String EXTRA_TEMPLATE_UUID = "com.app.timerlistactivity.extra_template_uuid";

    public static Intent newInstance(Context context, UUID id) {
        Intent intent = new Intent(context, TimerListActivity.class);
        intent.putExtra(EXTRA_TEMPLATE_UUID, id);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs_common);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.programs_fragment_container);
        if(fragment == null) {
            fragment = TimerListFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_TEMPLATE_UUID));
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
