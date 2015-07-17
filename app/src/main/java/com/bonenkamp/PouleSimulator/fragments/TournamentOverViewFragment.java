package com.bonenkamp.PouleSimulator.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.activities.TournamentSimulationActivity;
import com.bonenkamp.PouleSimulator.data.UpdateTeamStats;
import com.bonenkamp.PouleSimulator.models.Team;
import com.bonenkamp.PouleSimulator.models.Tournament;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TournamentOverViewFragment.OnNextMatchListener} interface
 * to handle interaction events.
 *
 * This Fragment shows an after match team standings.
 */
public class TournamentOverViewFragment extends Fragment implements View.OnClickListener {

    private OnNextMatchListener onNextMatchListener;

    private Tournament tournament;

    public TournamentOverViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            tournament  = bundle.getParcelable("tournament");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_over_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set on clicklistener on button
        Button btnNextMatch = (Button) getView().findViewById(R.id.next_match);
        btnNextMatch.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Set views with poule result
        setTournamentResultsView();
    }

    @Override
    public void onPause() {
        super.onPause();

        // save tournament variable
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putParcelable("tournament", tournament);

        onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onNextMatchListener = (OnNextMatchListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextMatchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNextMatchListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_match:
                if(tournament.setNextMatch()){
                    onNextMatchListener.onNextMatchListener(tournament);
                }else{
                    onNextMatchListener.onShowTournamentResults(tournament);
                }

                break;
        }
    }

    private void setTournamentResultsView() {
        TextView txtViewPosition1 = (TextView)  getView().findViewById(R.id.tournament_position_1);
        TextView txtViewPosition2 = (TextView)  getView().findViewById(R.id.tournament_position_2);
        TextView txtViewPosition3 = (TextView)  getView().findViewById(R.id.tournament_position_3);
        TextView txtViewPosition4 = (TextView)  getView().findViewById(R.id.tournament_position_4);

        ArrayList<Team> tournamentPositions = tournament.getSortedTournamentPositions();
        txtViewPosition1.setText(tournamentPositions.get(0).name() + " Points: " +
                tournamentPositions.get(0).getPoints());

        txtViewPosition2.setText(tournamentPositions.get(1).name() + " Points: " +
                tournamentPositions.get(1).getPoints());

        txtViewPosition3.setText(tournamentPositions.get(2).name() + " Points: " +
                tournamentPositions.get(2).getPoints());

        txtViewPosition4.setText(tournamentPositions.get(3).name() + " Points: " +
                tournamentPositions.get(3).getPoints());
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
    public interface OnNextMatchListener {
        /**
         * Start next match simulation
         * @param tournament current tournament object
         */
        public void onNextMatchListener(Tournament tournament);

        /**
         * Show tournament results (all matches played)
         * @param tournament
         */
        public void onShowTournamentResults(Tournament tournament);
    }


}
