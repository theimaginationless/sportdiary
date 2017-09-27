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
import android.view.View;

import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.UUID;

/**
 * Created by Dmitry on 25.07.2017.
 */

public class ProgramActivity extends AppCompatActivity {

    private static final String EXTRA_EXERCISE_UUID = "com.app.extra_exercise_uuid";
    private Toolbar mToolbar;

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
        setContentView(R.layout.activity_common);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8664711496353901~9647864045");
        AdView mAdView = (AdView) findViewById(R.id.adView_newday);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.VISIBLE);
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
