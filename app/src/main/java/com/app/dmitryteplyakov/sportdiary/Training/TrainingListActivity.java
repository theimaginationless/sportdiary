package com.app.dmitryteplyakov.sportdiary.Training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class TrainingListActivity extends AppCompatActivity {
    public static final String EXTRA_DAY_UUID = "com.app.traininglistActivity.extra_day_uuid";

    public static Intent newIntent(Context contextPackage, UUID dayId) {
        Intent intent = new Intent(contextPackage, TrainingListActivity.class);
        intent.putExtra(EXTRA_DAY_UUID, dayId);
        return intent;
    }

    protected Fragment createFragment() {
        return TrainingListFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_DAY_UUID));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.common_fragment_container);
        if(fragment == null) {
            fragment = new TrainingListFragment();
            fm.beginTransaction()
                    .add(R.id.common_fragment_container, fragment)
                    .commit();
        }
    }
}
