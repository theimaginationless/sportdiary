package com.app.dmitryteplyakov.sportdiary.Training;

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
 * Created by dmitry21 on 03.08.17.
 */

public class ExerciseListActivity extends AppCompatActivity {

    private static final String EXTRA_DAY_UUID = "com.app.exerciselistactivity.extra_day_uuid";

    public static Intent newIntent(Context contextPackage, UUID dayId) {
        Intent intent = new Intent(contextPackage, ExerciseListActivity.class);
        intent.putExtra(EXTRA_DAY_UUID, dayId);
        return intent;
    }

    protected Fragment createFragment() {
        return ExerciseListFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_DAY_UUID));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

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
