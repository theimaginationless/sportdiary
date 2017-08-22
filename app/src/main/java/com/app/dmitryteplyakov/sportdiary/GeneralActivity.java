package com.app.dmitryteplyakov.sportdiary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Weight.Weight;
import com.app.dmitryteplyakov.sportdiary.Core.Weight.WeightStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.TitlePickerFragment;
import com.app.dmitryteplyakov.sportdiary.Dialogs.WeightPickerFragment;
import com.app.dmitryteplyakov.sportdiary.Nutrition.NutritionDaysListFragment;
import com.app.dmitryteplyakov.sportdiary.Nutrition.NutritionListActivity;
import com.app.dmitryteplyakov.sportdiary.Overview.OverviewFragment;
import com.app.dmitryteplyakov.sportdiary.Programs.ProgramsListActivity;
import com.app.dmitryteplyakov.sportdiary.Settings.SettingsActivity;
import com.app.dmitryteplyakov.sportdiary.Timer.TimerTemplatesListActivity;
import com.app.dmitryteplyakov.sportdiary.Training.DaysListFragment;
import com.app.dmitryteplyakov.sportdiary.Training.NewDayActivity;
import com.app.dmitryteplyakov.sportdiary.Weight.WeightListFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.Nutrition.NutritionDaysListFragment.isDoneToday;

public class GeneralActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mFab;
    private static final int REQUEST_ADD_DAY = 2;
    private static final String SAVE_STATE = "com.app.generalactivity.save_state";
    private int TAB_STATE;
    private String[] mDrawerTitles;
    private ListView mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private TextView mUsernameTextView;
    private BottomBar mBottomBar;

    protected Fragment createFragment() {
        return new OverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GeneralActivity.this);
        mBottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        checkBadges(R.id.action_nutrition_tab, R.id.action_weight_tab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_general);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mUsernameTextView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.username);
        mUsernameTextView.setText(sharedPreferences.getString("username_nav_draw", getString(R.string.username_default)));
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.programs:
                        intent = new Intent(GeneralActivity.this, ProgramsListActivity.class);
                        break;
                    case R.id.timer_templates:
                        intent = new Intent(GeneralActivity.this, TimerTemplatesListActivity.class);
                        break;
                    case R.id.settings:
                        intent = new Intent(GeneralActivity.this, SettingsActivity.class);
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.START);
                if(intent != null) {
                    startActivity(intent);
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        mFab = (FloatingActionButton) findViewById(R.id.activity_common_fab_add);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId) {
                    case R.id.action_overview_tab:
                        onOverviewTab();
                        break;
                    case R.id.action_training_tab:
                        onTrainingTab();
                        break;
                    case R.id.action_nutrition_tab:
                        onNutritionTab();
                        break;
                    case R.id.action_weight_tab:
                        onWeightTab();
                        break;
                }
                checkBadges(R.id.action_nutrition_tab, R.id.action_weight_tab);
            }
        });

        //mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        /*mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
        });*/
        //Todo: Fix drop list position after rotation
        /*if(savedInstanceState != null) {
            TAB_STATE = savedInstanceState.getInt(SAVE_STATE);
            if(TAB_STATE == 0)
                mBottomNavigationView.setSelectedItemId(R.id.action_overview_tab);
            else if(TAB_STATE == 1)
                mBottomNavigationView.setSelectedItemId(R.id.action_training_tab);
            else if(TAB_STATE == 2)
                mBottomNavigationView.setSelectedItemId(R.id.action_nutrition_tab);
        }*/
        if(savedInstanceState != null) {
            TAB_STATE = savedInstanceState.getInt(SAVE_STATE);
            switch(TAB_STATE) {
                case 0:
                    mBottomBar.selectTabWithId(R.id.action_overview_tab);
                    break;
                case 1:
                    mBottomBar.selectTabWithId(R.id.action_training_tab);
                    break;
                case 2:
                    mBottomBar.selectTabWithId(R.id.action_nutrition_tab);
                    break;
                case 3:
                    mBottomBar.selectTabWithId(R.id.action_weight_tab);
                    break;
            }
            checkBadges(R.id.action_nutrition_tab, R.id.action_weight_tab);
        }


    }
    private void checkBadges(int ... tabRes) {
        for(int res : tabRes) {
            BottomBarTab tab = mBottomBar.getTabWithId(res);
            boolean check = false;
            if(res == R.id.action_nutrition_tab)
                check = NutritionDaysListFragment.isDoneToday(this);
            else if(res == R.id.action_weight_tab)
                check = WeightListFragment.isDoneToday(this);
            if (check) {
                tab.setBadgeCount(1);
                try {
                    Field badgeFieldDefinition = tab.getClass().getDeclaredField("badge");
                    badgeFieldDefinition.setAccessible(true);
                    TextView badgeTextView = (TextView) badgeFieldDefinition.get(tab);
                    badgeTextView.setText("");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Log.d("NDLF", "Exception", e);
                }
            } else
                tab.setBadgeCount(0);
        }
    }

    private void onOverviewTab() {
        TAB_STATE = 0;
        Fragment fragment = new OverviewFragment();
        mFab.hide();
        getSupportFragmentManager().beginTransaction()
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
                    Date currentDate = new Date();
                    if (DayStorage.get(GeneralActivity.this).getDayByDate(currentDate) == null) {
                        Day day = new Day();
                        day.setTrainingId(UUID.randomUUID()); // Temporary trainingUUID as hack
                        DayStorage.get(GeneralActivity.this).addDay(day);
                        Intent intent = NewDayActivity.newIntent(GeneralActivity.this, day.getId());
                        startActivityForResult(intent, REQUEST_ADD_DAY);
                    } else
                        Snackbar.make(findViewById(R.id.snackbar_place), getString(R.string.snackbar_already_training), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onNutritionTab() {
        TAB_STATE = 2;
        Fragment fragment = new NutritionDaysListFragment();
        mFab.show();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                if(NutritionDayStorage.get(GeneralActivity.this).getNutritionDayByDate(currentDate) == null) {
                    NutritionDay nutritionDay = new NutritionDay(UUID.randomUUID());
                    NutritionDayStorage.get(GeneralActivity.this).addNutritionDay(nutritionDay);
                    Intent intent;
                    intent = NutritionListActivity.newIntent(GeneralActivity.this, nutritionDay.getId());
                    startActivity(intent);
                    //Todo: To strings.xml
                } else
                    Snackbar.make(findViewById(R.id.snackbar_place), getString(R.string.snackbar_already_nutrition), Snackbar.LENGTH_SHORT).show();
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onWeightTab() {
        TAB_STATE = 3;
        Fragment fragment = new WeightListFragment();
        mFab.show();
        /*mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weight weight = new Weight();
                WeightStorage.get(GeneralActivity.this).addWeight(weight);
                Intent intent;
                intent = NutritionListActivity.newIntent(GeneralActivity.this, weight.getId());
                startActivity(intent);

            }
        });*/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SAVE_STATE, TAB_STATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            default:
                //
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else
            super.onBackPressed();
    }


}
