package com.bonenkamp.PouleSimulator.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.data.UpdateTeamStats;
import com.bonenkamp.PouleSimulator.logic.MatchSimulation;
import com.bonenkamp.PouleSimulator.models.Match;
import com.bonenkamp.PouleSimulator.models.Tournament;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TournamentMatchFragment.OnTournamentOverviewListener} interface
 * to handle interaction events.
 *
 * In this fragment a tournament match will be simulated.
 */
public class TournamentMatchFragment extends Fragment implements View.OnClickListener {

    private OnTournamentOverviewListener onTournamentOverviewListener;

    private Tournament tournament;

    private MatchSimulation matchSimulation;

    private StartSimulationAsync startSimulationAsync;

    private int startTime;


    public TournamentMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        Bundle bundle = getArguments();
        if(bundle != null) {
            tournament  = bundle.getParcelable("tournament");
            startTime   = bundle.getInt("startTime");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_match, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Hide 'continue to overview' button
        Button btnContinue = (Button) getView().findViewById(R.id.btn_to_tournament_overview);
        btnContinue.setVisibility(View.INVISIBLE);
        btnContinue.setOnClickListener(this);

        //Start the Asynctask which setups and starts the simulation.
        startSimulationAsync = new StartSimulationAsync();
        startSimulationAsync.execute();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("startTimeOnPause", String.valueOf(startTime));

        // Stop simulation
        matchSimulation.stopSimulation();

        // Stop asynctask
        startSimulationAsync.cancel(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTournamentOverviewListener = (OnTournamentOverviewListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onNextTournamentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onTournamentOverviewListener = null;
    }

    /**
     * On button clicks
     * @param v View that calls this method
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_tournament_overview:

                // Notify and send tournament object to activity, which sends
                // the data to and loads the appropriate fragment.
                onTournamentOverviewListener.onTournamentOverviewListener(tournament);
                break;
            default:
                break;
        }
    }

    /**
     * {@link AsyncTask} sets up textviews in onPreExecute() and runs the simulation in the
     * doInBackground method. This method is public so it can be accessed from the
     * {@link MatchSimulation} class to post updates to the ui. Also it stores the
     * goals in the database via {@link UpdateTeamStats#UpdateTeamStats()}
     */
    public class StartSimulationAsync extends AsyncTask<Void, String, Boolean> {

        private TextView txtViewTime    = (TextView) getView().findViewById(R.id.match_time);
        private TextView txtViewTeams   = (TextView) getView().findViewById(R.id.match_teams);
        private TextView txtViewScore   = (TextView) getView().findViewById(R.id.match_score);

        private Match currentMatch;

        @Override
        protected void onPreExecute() {
            txtViewTime.setText("0''");

            // get the new to simulate match from tournament collection
            currentMatch = tournament.newMatch();

            txtViewTeams.setText(currentMatch.getMatchName());
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(!isCancelled()) {
                if (!currentMatch.isMatchPlayed()) {

                    // Create new simulation instance
                    matchSimulation = new MatchSimulation();

                    // StartSimulation
                    matchSimulation.startSimulation(currentMatch, startTime, this);

                    // Store the goals in the database
                    new UpdateTeamStats().updateTeamStats(currentMatch.teamHome().id(),
                            currentMatch.goalsTeamHome(),
                            currentMatch.goalsTeamAway(),
                            getActivity());

                    new UpdateTeamStats().updateTeamStats(currentMatch.teamAway().id(),
                            currentMatch.goalsTeamAway(),
                            currentMatch.goalsTeamHome(),
                            getActivity());

                    return true;
                }
            }
            return false;
        }

        public void progress(String... progress) {
            this.publishProgress(progress);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            //Parameter is array
            txtViewTime.setText(progress[0] + "''");
            txtViewScore.setText(progress[1]);

            startTime = Integer.parseInt(progress[0]);
        }



        @Override
        protected void onPostExecute(Boolean result) {
            //Simulation done, post results and match statistics.
            txtViewScore.setText(currentMatch.getCurrentResult());

            TextView txtViewStats = (TextView) getView().findViewById(R.id.match_stats);

            txtViewStats.setText("Ballpossession "
                            + currentMatch.teamHome().name()
                            + ": "
                            + currentMatch.getBallPossessionHome()
                            +"% \n"
                            + "Ballpossession "
                            + currentMatch.teamAway().name()
                            + ": "
                            + currentMatch.getBallPossessionAway()
                            + "%"
            );

            // Show 'continue to overview' button
            Button btnContinue = (Button) getView().findViewById(R.id.btn_to_tournament_overview);
            btnContinue.setVisibility(View.VISIBLE);

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
    public interface OnTournamentOverviewListener {
        public void onTournamentOverviewListener(Tournament tournament);
    }

}
