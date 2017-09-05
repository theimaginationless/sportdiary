package com.app.dmitryteplyakov.sportdiary.Training;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.dmitryteplyakov.sportdiary.R;
import com.app.dmitryteplyakov.sportdiary.Timer.TimerDisplayActivity;

import java.util.UUID;

/**
 * Created by dmitry21 on 03.08.17.
 */
//Todo: Exercise fragment on day
public class ExerciseActivity extends AppCompatActivity {
    private static final String EXTRA_TRAINING_UUID = "com.app.exerciseactivity.extra_training_uuid";
    private Toolbar mToolbar;

    public static Intent newIntent(Context contextPackage, UUID trainingId) {
        Intent intent = new Intent(contextPackage, ExerciseActivity.class);
        intent.putExtra(EXTRA_TRAINING_UUID, trainingId);
        return intent;
    }

    protected Fragment createFragment() {
        return ExerciseFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_TRAINING_UUID));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ExerciseActivity.this);
        boolean timerEnable = sharedPreferences.getBoolean("switch_on_timer", true);
        if(timerEnable)
            getMenuInflater().inflate(R.menu.current_exercise_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.start_timer:
                Intent intent = new Intent(ExerciseActivity.this, TimerDisplayActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
