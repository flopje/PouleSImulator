package com.bonenkamp.PouleSimulator.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.data.LoadReferees;
import com.bonenkamp.PouleSimulator.models.Match;
import com.bonenkamp.PouleSimulator.models.Referee;
import com.bonenkamp.PouleSimulator.models.Team;
import com.bonenkamp.PouleSimulator.models.Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TournamentSetUpFragment.OnTournamentStartMatchListener} interface
 * to handle interaction events.

 * Setup the tournament with provided team. E.g creates competition scheme.
 */
public class TournamentSetUpFragment extends Fragment implements View.OnClickListener {

    private OnTournamentStartMatchListener tournamentStartMatchListener;

    private ArrayList<Team> teams               = new ArrayList<>();
    private ArrayList<Match> tournamentArray    = new ArrayList<>();
    private ArrayList<Referee> allReferees      = new ArrayList<>();

    private Tournament tournament;


    public TournamentSetUpFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null) {
            teams = bundle.getParcelableArrayList("teams");
        }

        return inflater.inflate(R.layout.fragment_tournament_simulation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Show dialog
        Dialog dialog = new ProgressDialog(getActivity());
        dialog.show();

        // Setup tournament matches
        setUpTournamentMatches();

        // Show match planning
        showMatchPlanning();

        // Show start tournament button
        Button btnStartTournament = (Button) getView().findViewById(R.id.btnStartTournament);
        btnStartTournament.setOnClickListener(this);

        dialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tournamentStartMatchListener = (OnTournamentStartMatchListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTournamentStartListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tournamentStartMatchListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTournament:

                // Notify and send tournament object to activity, which sends
                // the data to and loads the appropriate fragment.
                tournamentStartMatchListener.startTournamentMatch(tournament);
                break;
            default:
                break;
        }
    }

    /**
     * Create a 'Single Round Robin Tournament', and adds each match to the {@link ArrayList<Match>}
     * tournament variable.
     */
    private void setUpTournamentMatches() {
        int k = 1;
        for(int i = 0; i < teams.size(); i++) {
            for(int j = i + 1; j < teams.size(); j++){
                Match match = new Match(k,teams.get(i), teams.get(j), getReferee());

                tournamentArray.add(match);
                k++;
            }
        }

        //Shuffle tournamentArray for more 'natural feel' of the order of the matches
        Collections.shuffle(tournamentArray);

        //Create new tournament object from shuffled ArrayList.
        tournament = new Tournament(
                tournamentArray.get(0),
                tournamentArray.get(1),
                tournamentArray.get(2),
                tournamentArray.get(3),
                tournamentArray.get(4),
                tournamentArray.get(5));
    }

    /**
     * Get one referee from all referees for use in match.
     * @return {@link Referee} the referee object.
     */
    private Referee getReferee() {
        if(allReferees.isEmpty()) {
            allReferees = LoadReferees.getAllRefereesFromDatabase(getActivity());
        }

        // Random number to get a random referee from the arraylist.
        Random randomNumber = new Random();
        int index = randomNumber.nextInt(allReferees.size());

        return allReferees.get(index);
    }

    /**
     * Show the matchNames in the ui
     */
    private void showMatchPlanning() {
        if(!tournamentArray.isEmpty()){
            TextView txtView1Home = (TextView) getView().findViewById(R.id.match1_teamHome);
            TextView txtView2Home = (TextView) getView().findViewById(R.id.match2_teamHome);
            TextView txtView3Home = (TextView) getView().findViewById(R.id.match3_teamHome);
            TextView txtView4Home = (TextView) getView().findViewById(R.id.match4_teamHome);
            TextView txtView5Home = (TextView) getView().findViewById(R.id.match5_teamHome);
            TextView txtView6Home = (TextView) getView().findViewById(R.id.match6_teamHome);

            TextView txtView1Away = (TextView) getView().findViewById(R.id.match1_teamAway);
            TextView txtView2Away = (TextView) getView().findViewById(R.id.match2_teamAway);
            TextView txtView3Away = (TextView) getView().findViewById(R.id.match3_teamAway);
            TextView txtView4Away = (TextView) getView().findViewById(R.id.match4_teamAway);
            TextView txtView5Away = (TextView) getView().findViewById(R.id.match5_teamAway);
            TextView txtView6Away = (TextView) getView().findViewById(R.id.match6_teamAway);

            txtView1Home.setText(tournament.match1().teamHome().name());
            txtView2Home.setText(tournament.match2().teamHome().name());
            txtView3Home.setText(tournament.match3().teamHome().name());
            txtView4Home.setText(tournament.match4().teamHome().name());
            txtView5Home.setText(tournament.match5().teamHome().name());
            txtView6Home.setText(tournament.match6().teamHome().name());

            txtView1Away.setText(tournament.match1().teamAway().name());
            txtView2Away.setText(tournament.match2().teamAway().name());
            txtView3Away.setText(tournament.match3().teamAway().name());
            txtView4Away.setText(tournament.match4().teamAway().name());
            txtView5Away.setText(tournament.match5().teamAway().name());
            txtView6Away.setText(tournament.match6().teamAway().name());
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTournamentStartMatchListener {
        public void startTournamentMatch(Tournament tournament);
    }
}
