package com.neige_i.mareu.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;

public abstract class BaseActivity extends AppCompatActivity {

    // ------------------------------------- ACTIVITY METHODS --------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Config title
        if (getSupportActionBar() == null)
            setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(getTitleId() != R.string.app_name);
        setTitle(getTitleId());

        // Config fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getFragment()).commitNow();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Reset repositories when configuration changes (orientation|screenSize)
        DI.getListingRepository().initRepository();
        DI.getMeetingRepository().initRepository();
    }

    // ------------------------------------- ABSTRACT METHODS --------------------------------------

    protected abstract int getTitleId();

    protected abstract Fragment getFragment();
}
