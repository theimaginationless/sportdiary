package com.app.dmitryteplyakov.sportdiary.Timer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerListActivity extends AppCompatActivity {
    private static final String EXTRA_TEMPLATE_UUID = "com.app.timerlistactivity.extra_template_uuid";

    public static Intent newInstance(Context context, UUID id) {
        Intent intent = new Intent(context, TimerListActivity.class);
        intent.putExtra(EXTRA_TEMPLATE_UUID, id);
        return intent;
    }

}
