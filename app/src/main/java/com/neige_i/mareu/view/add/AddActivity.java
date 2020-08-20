package com.neige_i.mareu.view.add;

import androidx.fragment.app.Fragment;

import com.neige_i.mareu.R;
import com.neige_i.mareu.view.BaseActivity;

public class AddActivity extends BaseActivity {

    @Override
    protected int getTitleId() {
        return R.string.new_meeting;
    }

    @Override
    protected Fragment getFragment() {
        return AddFragment.newInstance();
    }
}