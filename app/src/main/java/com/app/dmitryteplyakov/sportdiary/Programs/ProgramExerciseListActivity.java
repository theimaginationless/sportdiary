package com.app.dmitryteplyakov.sportdiary.Programs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 27.07.2017.
 */

public class ProgramExerciseListActivity extends AppCompatActivity {

    public static final String EXTRA_TRAINING_UUID = "com.app.extra_training_uuid";

    public static Intent newIntent(Context contextPackage, UUID trainingId) {
        Intent intent = new Intent(contextPackage, ProgramExerciseListActivity.class);
        intent.putExtra(EXTRA_TRAINING_UUID, trainingId);
        Log.d("PELA", "SCHEMA");
        return intent;
    }

    protected Fragment createFragment() {
        return ProgramExerciseListFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_TRAINING_UUID));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs_common);

        Log.d("PELA", "CALL PELA ONCREATE!");
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.programs_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.programs_fragment_container, fragment)
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
