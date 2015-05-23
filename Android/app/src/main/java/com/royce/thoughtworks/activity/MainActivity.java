
package com.royce.thoughtworks.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.royce.thoughtworks.R;
import com.royce.thoughtworks.datasource.DataSource;
import com.royce.thoughtworks.fragment.AddReminder;
import com.royce.thoughtworks.fragment.HistoryFragment;
import com.royce.thoughtworks.fragment.HomeFragment;
import com.royce.thoughtworks.fragment.NavigationDrawerFragment;
import com.royce.thoughtworks.fragment.SettingsFragment;
import com.royce.thoughtworks.fragment.StationCodesFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = HomeFragment.newInstance();
                break;
            case 1:
                fragment = AddReminder.newInstance(false);
                break;
            case 2:
                fragment = AddReminder.newInstance(true);
                break;
            case 3:
                fragment = HistoryFragment.newInstance();
                break;
            case 4:
                fragment = StationCodesFragment.newInstance();
                break;
            case 5:
                fragment = SettingsFragment.newInstance();
                break;
            case 6:
                fragment = SettingsFragment.newInstance();
                break;

        }
        onSectionAttached(position);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.home);
                break;
            case 1:
                mTitle = getString(R.string.add_a_reminder);
                break;
            case 2:
                mTitle = getString(R.string.wake_me_up);
                break;
            case 3:
                mTitle = getString(R.string.history);
                break;
            case 4:
                mTitle = getString(R.string.codes);
                break;
            case 5:
                mTitle = getString(R.string.about_us);
                break;
            case 6:
                mTitle = getString(R.string.settings);
                break;
        }
    }

    public static void switchToHome() {

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
