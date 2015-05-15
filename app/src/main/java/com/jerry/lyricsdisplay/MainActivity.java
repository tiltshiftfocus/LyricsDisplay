package com.jerry.lyricsdisplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.jerry.lyricsdisplay.fragments.DisplayLyricFragment;
import com.jerry.lyricsdisplay.fragments.NavDrawerFragment;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Fragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        NavDrawerFragment drawerFragment = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer1);
        drawerFragment.setUp(R.id.nav_drawer1, (DrawerLayout) findViewById(R.id.drawer_layout1), toolbar);

        Fragment displayFragment = new DisplayLyricFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_relativelayout, displayFragment).commit();

    }

}
