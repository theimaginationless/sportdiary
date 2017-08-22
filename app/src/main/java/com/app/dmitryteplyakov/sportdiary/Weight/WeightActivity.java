package com.app.dmitryteplyakov.sportdiary.Weight;

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
 * Created by dmitry21 on 22.08.17.
 */

public class WeightActivity extends AppCompatActivity {
    private static final String EXTRA_WEIGHT_UUID = "com.app.weightactivity.extra_weighg_uuid";

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, WeightActivity.class);
        intent.putExtra(EXTRA_WEIGHT_UUID, id);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.common_fragment_container);
        if(fragment == null) {
            fragment = WeightFragment.newInstance((UUID) getIntent().getSerializableExtra(EXTRA_WEIGHT_UUID));
            fm.beginTransaction()
                    .add(R.id.common_fragment_container, fragment)
                    .commit();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
