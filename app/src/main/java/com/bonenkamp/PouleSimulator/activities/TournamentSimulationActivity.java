package com.bonenkamp.PouleSimulator.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.fragments.TournamentMatchFragment;
import com.bonenkamp.PouleSimulator.fragments.TournamentOverViewFragment;
import com.bonenkamp.PouleSimulator.fragments.TournamentResultsFragment;
import com.bonenkamp.PouleSimulator.fragments.TournamentSetUpFragment;
import com.bonenkamp.PouleSimulator.models.Match;
import com.bonenkamp.PouleSimulator.models.Team;
import com.bonenkamp.PouleSimulator.models.Tournament;

import java.util.ArrayList;

/**
 * This is a container activity for all match and tournament simulation related fragments.
 */
public class TournamentSimulationActivity extends ActionBarActivity implements
        TournamentSetUpFragment.OnTournamentStartMatchListener,
        TournamentMatchFragment.OnTournamentOverviewListener,
        TournamentOverViewFragment.OnNextMatchListener,
        TournamentResultsFragment.OnTournamentResultsListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView  (R.layout.activity_tournament_simulation);

        Intent intent = getIntent();
        ArrayList<Team> teams = intent.getParcelableArrayListExtra("teams");

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("teams", teams);

        TournamentSetUpFragment fragment = new TournamentSetUpFragment();
        fragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No optionsmenu available.
//        getMenuInflater().inflate(R.menu.menu_tournament_simulation, menu);
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
    public void onResume() {
        super.onResume();
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
    public void startTournamentMatch(Tournament tournament) {
        //Next fragment, for match simulation
        TournamentMatchFragment fragmentMatch = new TournamentMatchFragment();

        // Add the teams, tournament setup and start match id to the bundle
        // so TournamentMatchFragment can use them.
        Bundle bundle = new Bundle();
        bundle.putParcelable("tournament", tournament);

        fragmentMatch.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //Replace current fragment with new fragment
        transaction.remove(getFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragmentMatch);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onTournamentOverviewListener(Tournament tournament) {
        TournamentOverViewFragment overViewFragment = new TournamentOverViewFragment();

        //Add the tournament object
        Bundle bundle = new Bundle();
        bundle.putParcelable("tournament", tournament);

        overViewFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if(overViewFragment.isAdded()){

        }else {
            //Remove current fragment
            transaction.remove(getFragmentManager().findFragmentById(R.id.fragment_container));//.commit();
            //Add new fragment
            transaction.add(R.id.fragment_container, overViewFragment);
            transaction.commit();
        }
    }

    @Override
    public void onNextMatchListener(Tournament tournament) {
        //Next fragment, for match simulation
        TournamentMatchFragment fragmentMatch = new TournamentMatchFragment();

        // Add the teams, tournament setup and start match id to the bundle
        // so TournamentMatchFragment can use them.
        Bundle bundle = new Bundle();
        bundle.putParcelable("tournament", tournament);

        fragmentMatch.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //Remove current fragment
        transaction.remove(getFragmentManager().findFragmentById(R.id.fragment_container));//.commit();

        // Add new fragment
        transaction.add(R.id.fragment_container, fragmentMatch);
        transaction.commit();
    }

    @Override
    public void onShowTournamentResults(Tournament tournament) {
        //Next fragment, show end results poule/ tournament
        TournamentResultsFragment fragmentResults = new TournamentResultsFragment();

        // Add the teams, tournament setup and start match id to the bundle
        // so TournamentMatchFragment can use them.
        Bundle bundle = new Bundle();
        bundle.putParcelable("tournament", tournament);

        fragmentResults.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //Remove current fragment
        transaction.remove(getFragmentManager().findFragmentById(R.id.fragment_container));

        // Add new fragment
        transaction.add(R.id.fragment_container, fragmentResults);
        transaction.commit();
    }

    @Override
    public void onBackToHomeInteraction() {
        // cal finish to return to mainActivity
        finish();
    }
}
