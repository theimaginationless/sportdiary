package com.app.dmitryteplyakov.sportdiary.Programs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 25.07.2017.
 */

public class ProgramActivity extends AppCompatActivity {

    private static final String EXTRA_EXERCISE_UUID = "com.app.extra_exercise_uuid";

    public static Intent newIntent(Context contextPackage, UUID exerciseId) {
        Intent intent = new Intent(contextPackage, ProgramActivity.class);
        intent.putExtra(EXTRA_EXERCISE_UUID, exerciseId);
        Log.d("PA", "EID: " + exerciseId.toString());
        Log.d("PA", "PID " + ExerciseStorage.get(contextPackage).getExercise(exerciseId).getParentUUID().toString());
        return intent;
    }

    protected Fragment createFragment() {
        return ProgramFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_EXERCISE_UUID));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs_common);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.programs_fragment_container);
        if(fragment == null) {
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
