package com.bonenkamp.PouleSimulator.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.fragments.CreateTeamFragment;
import com.bonenkamp.PouleSimulator.fragments.MainFragment;
import com.bonenkamp.PouleSimulator.fragments.TeamsInfoFragment;

/**
 * Main activity. Used for ap start up.
 */
public class MainActivity extends ActionBarActivity implements MainFragment.OnCreateTeamListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //No menu
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCreateTeam() {
        CreateTeamFragment fragment = new CreateTeamFragment();

        FragmentTransaction transaction =  getFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);

        //add fragment to backstack
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onViewTeamStats() {
        TeamsInfoFragment infoFragment = new TeamsInfoFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container, infoFragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
