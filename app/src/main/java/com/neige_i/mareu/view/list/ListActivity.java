package com.neige_i.mareu.view.list;

import androidx.fragment.app.Fragment;

import com.neige_i.mareu.R;
import com.neige_i.mareu.view.BaseActivity;

public class ListActivity extends BaseActivity {

    @Override
    protected int getTitleId() {
        return R.string.app_name;
    }

    @Override
    protected Fragment getFragment() {
        return ListFragment.newInstance();
    }
}