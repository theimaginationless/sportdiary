package com.app.dmitryteplyakov.sportdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GeneralActivity extends AppCompatActivity {

    private FloatingActionButton mFab;
    private static final int REQUEST_ADD_DAY = 2;
    private static final String SAVE_STATE = "com.app.generalactivity.save_state";
    private int TAB_STATE;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private TextView mUsernameTextView;
    private BottomBar mBottomBar;
    private Intent mIntentToSet;

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
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(mIntentToSet != null) {
                    startActivity(mIntentToSet);
                }
                mIntentToSet = null;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.programs:
                        mIntentToSet = new Intent(GeneralActivity.this, ProgramsListActivity.class);
                        break;
                    case R.id.timer_templates:
                        mIntentToSet = new Intent(GeneralActivity.this, TimerTemplatesListActivity.class);
                        break;
                    case R.id.settings:
                        mIntentToSet = new Intent(GeneralActivity.this, SettingsActivity.class);
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.START);

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

        //Todo: Fix drop list position after rotation
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(GeneralActivity.this);
        Set<String> enabledValues = sp.getStringSet("badges_value", new HashSet<String>(Arrays.asList(getString(R.string.action_nutrition_tab_title), getString(R.string.action_nutrition_tab_title))));
        Boolean isEnabled = sp.getBoolean("switch_on_badges", true);
        try {
                for (int res : tabRes) {
                    BottomBarTab tab = mBottomBar.getTabWithId(res);
                    if (isEnabled) {

                        boolean check = false;
                        if (res == R.id.action_nutrition_tab && enabledValues.contains(getString(R.string.action_nutrition_tab_title)))
                            check = NutritionDaysListFragment.isDoneToday(this);
                        else if (res == R.id.action_weight_tab && enabledValues.contains(getString(R.string.action_weight_tab_title)))
                            check = WeightListFragment.isDoneToday(this);
                        if (check) {
                            tab.setBadgeCount(1);
                            try {
                                Field badgeFieldDefinition = tab.getClass().getDeclaredField("badge");
                                badgeFieldDefinition.setAccessible(true);
                                TextView badgeTextView = (TextView) badgeFieldDefinition.get(tab);
                                badgeTextView.setText("");
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                            }
                        } else
                            tab.setBadgeCount(0);
                    } else {
                        Log.d("GA", "Badges are disabled!");
                        tab.setBadgeCount(0);
                    }
                }

        } catch(NullPointerException e) {
            Log.e("GA", "enabledValues exception!", e);
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if ((TrainingStorage.get(GeneralActivity.this).getTrainings().size() == 0) && (ExerciseStorage.get(GeneralActivity.this).getExercises().size() == 0))
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
                thread.start();

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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                        Log.d("GA", "Nutrition in thread!");
                    }
                });
                thread.start();

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBadges(R.id.action_nutrition_tab, R.id.action_weight_tab);
        Log.d("GA", "Resume");
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else
            super.onBackPressed();
    }


}
