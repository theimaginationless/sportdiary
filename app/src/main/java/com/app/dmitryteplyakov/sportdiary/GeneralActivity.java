package com.app.dmitryteplyakov.sportdiary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Nutrition.NutritionDaysListFragment;
import com.app.dmitryteplyakov.sportdiary.Overview.OverviewFragment;
import com.app.dmitryteplyakov.sportdiary.Programs.ProgramsListActivity;
import com.app.dmitryteplyakov.sportdiary.Settings.SettingsActivity;
import com.app.dmitryteplyakov.sportdiary.Training.DaysListFragment;
import com.app.dmitryteplyakov.sportdiary.Training.NewDayActivity;

import java.util.UUID;

public class GeneralActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mFab;
    private static final int REQUEST_ADD_DAY = 2;
    private static final String SAVE_STATE = "com.app.generalactivity.save_state";
    private int TAB_STATE;

    protected Fragment createFragment() {
        return new OverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mFab = (FloatingActionButton) findViewById(R.id.activity_common_fab_add);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_overview_tab:
                        onOverviewTab();
                        break;
                    case R.id.action_training_tab:
                        onTrainingTab();
                        break;
                    case R.id.action_nutrition_tab:
                        onNutritionTab();
                        break;
                }
                return true;
            }
        });
        //Todo: Fix drop list position after rotation
        if(savedInstanceState != null) {
            TAB_STATE = savedInstanceState.getInt(SAVE_STATE);
            if(TAB_STATE == 0)
                mBottomNavigationView.setSelectedItemId(R.id.action_overview_tab);
            else if(TAB_STATE == 1)
                mBottomNavigationView.setSelectedItemId(R.id.action_training_tab);
            else if(TAB_STATE == 2)
                mBottomNavigationView.setSelectedItemId(R.id.action_nutrition_tab);
        }
    }

    private void onOverviewTab() {
        TAB_STATE = 0;
        Fragment fragment = new OverviewFragment();
        mFab.hide();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onTrainingTab() {
        TAB_STATE = 1;
        Fragment fragment = new DaysListFragment();
        mFab.show();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GA", "CALLED ONCLICKLISTENER!");
                if((TrainingStorage.get(GeneralActivity.this).getTrainings().size() == 0) && (ExerciseStorage.get(GeneralActivity.this).getExercises().size() == 0))
                    Snackbar.make(findViewById(R.id.snackbar_place), getString(R.string.training_is_empty_snackbar), Snackbar.LENGTH_LONG)
                            .setAction(R.string.add, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(GeneralActivity.this, ProgramsListActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                else {
                    Day day = new Day();
                    day.setTrainingId(UUID.randomUUID()); // Temporary trainingUUID as hack
                    DayStorage.get(GeneralActivity.this).addDay(day);
                    Intent intent = NewDayActivity.newIntent(GeneralActivity.this, day.getId());
                    startActivityForResult(intent, REQUEST_ADD_DAY);
                }
            }
        });
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onNutritionTab() {
        TAB_STATE = 2;
        Fragment fragment = new NutritionDaysListFragment();
        mFab.show();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SAVE_STATE, TAB_STATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.settings:
                intent = new Intent(GeneralActivity.this, SettingsActivity.class);
                break;
            case R.id.programs:
                intent = new Intent(GeneralActivity.this, ProgramsListActivity.class);
                break;
            default:
                //
        }
        if(intent != null)
            startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
