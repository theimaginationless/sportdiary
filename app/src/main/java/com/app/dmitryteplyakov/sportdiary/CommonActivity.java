package com.app.dmitryteplyakov.sportdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dmitry on 22.07.2017.
 */

public abstract class CommonActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    protected abstract int getFragmentContainer();
    protected abstract int getActivityLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainer());
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(getFragmentContainer(), fragment)
                    .commit();
        }
    }
}
