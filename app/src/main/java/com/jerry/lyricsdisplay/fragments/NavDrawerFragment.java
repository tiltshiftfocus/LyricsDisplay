package com.jerry.lyricsdisplay.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jerry.lyricsdisplay.R;

public class NavDrawerFragment extends Fragment {

    final public static String PREFERENCES_FILE = "nav_drawer_pref";
    final public static String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearned;
    private boolean mFromSavedState;

    private View containerView;
    private ListView listViewChoices;

    private String[] choices;

    public NavDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mUserLearned = Boolean.valueOf(readFromPref(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if (savedInstance != null) {
            mFromSavedState = true;
        }

        choices = getActivity().getResources().getStringArray(R.array.nav_drawer_choices);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_drawer, container, false);
    }

    public void setUp(int fragmentID, DrawerLayout drawer, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);

        mDrawerLayout = drawer;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearned) {
                    mUserLearned = true;
                    saveToPref(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearned + "");
                }
                getActivity().invalidateOptionsMenu();
                getActivity().setTitle(getText(R.string.app_name));

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

        };

        if (!mUserLearned && !mFromSavedState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // --- Show Hamburger Menu ---
        // Basically the ActionBarDrawerToggle
        // needs to sync with the drawer fragment itself
        // for animation, etc
        // which requires Runnable
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.choices_list, choices);
        listViewChoices = (ListView) containerView.findViewById(R.id.navdrawer_listview1);
        listViewChoices.setAdapter(adapter);
        listViewChoices.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewChoices.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new DisplayLyricFragment();
        switch(position){
            case 0:
                fragment = new DisplayLyricFragment();
                break;
            case 1:
                fragment = new SetLyricFragment();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_relativelayout, fragment).commit();

        // update selected item and title, then close the drawer
        listViewChoices.setItemChecked(position, true);
        getActivity().setTitle(choices[listViewChoices.getCheckedItemPosition()]);
        mDrawerLayout.closeDrawer(containerView);
    }


    public static void saveToPref(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readFromPref(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }


}
