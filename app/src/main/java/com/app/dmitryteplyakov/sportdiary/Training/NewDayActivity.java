package com.app.dmitryteplyakov.sportdiary.Training;

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
 * Created by Dmitry on 01.08.2017.
 */

public class NewDayActivity extends AppCompatActivity {
    private static final String EXTRA_NEW_DAY_UUID = "com.app.newdayactivity.extra_new_day_uuid";
    private Toolbar mToolbar;

    public static Intent newIntent(Context contextPackage, UUID id) {
        Intent intent = new Intent(contextPackage, NewDayActivity.class);
        intent.putExtra(EXTRA_NEW_DAY_UUID, id);
        return intent;
    }

    public Fragment createFragment() {
        return NewDayFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_NEW_DAY_UUID));
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

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
