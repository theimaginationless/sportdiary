package com.app.dmitryteplyakov.sportdiary.Nutrition;

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
 * Created by dmitry21 on 14.08.17.
 */

public class NutritionListActivity extends AppCompatActivity {
    private static final String EXTRA_NUTRITION_DAY_UUID = "com.app.extra_nutrition_day_uuid";

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, NutritionListActivity.class);
        intent.putExtra(EXTRA_NUTRITION_DAY_UUID, id);
        return intent;
    }

    public Fragment createFragment() {
        return NutritionListFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_NUTRITION_DAY_UUID));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.common_fragment_container);
        if (fragment == null) {
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
