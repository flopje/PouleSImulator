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
import com.bonenkamp.PouleSimulator.models.Team;
import com.bonenkamp.PouleSimulator.models.Tournament;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TournamentResultsFragment.OnTournamentResultsListener} interface
 * to handle interaction events.
 *
 * Shows the final tournament result.
 */
public class TournamentResultsFragment extends Fragment implements View.OnClickListener {

    private OnTournamentResultsListener mListener;

    private Tournament tournament;

    public TournamentResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournament = getArguments().getParcelable("tournament");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_results, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btnGoToHome = (Button) getView().findViewById(R.id.btn_tournament_results_toHome);
        btnGoToHome.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        setTournamentResultsView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTournamentResultsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setTournamentResultsView() {
        TextView txtViewPosition1 = (TextView)  getView().findViewById(R.id.textview_result_1);
        TextView txtViewPosition2 = (TextView)  getView().findViewById(R.id.textview_result_2);
        TextView txtViewPosition3 = (TextView)  getView().findViewById(R.id.textview_result_3);
        TextView txtViewPosition4 = (TextView)  getView().findViewById(R.id.textview_result_4);

        TextView txtViewPosition1Points = (TextView)  getView().findViewById(R.id.textview_result_1_points);
        TextView txtViewPosition2Points = (TextView)  getView().findViewById(R.id.textview_result_2_points);
        TextView txtViewPosition3Points = (TextView)  getView().findViewById(R.id.textview_result_3_points);
        TextView txtViewPosition4Points = (TextView)  getView().findViewById(R.id.textview_result_4_points);

        TextView txtViewPosition1Goals = (TextView)  getView().findViewById(R.id.textview_result_1_goals);
        TextView txtViewPosition2Goals = (TextView)  getView().findViewById(R.id.textview_result_2_goals);
        TextView txtViewPosition3Goals = (TextView)  getView().findViewById(R.id.textview_result_3_goals);
        TextView txtViewPosition4Goals = (TextView)  getView().findViewById(R.id.textview_result_4_goals);

        ArrayList<Team> tournamentPositions = tournament.getSortedTournamentPositions();
        txtViewPosition1.setText(tournamentPositions.get(0).name());
        txtViewPosition2.setText(tournamentPositions.get(1).name());
        txtViewPosition3.setText(tournamentPositions.get(2).name());
        txtViewPosition4.setText(tournamentPositions.get(3).name());

        txtViewPosition1Points.setText(String.valueOf(tournamentPositions.get(0).getPoints()));
        txtViewPosition2Points.setText(String.valueOf(tournamentPositions.get(1).getPoints()));
        txtViewPosition3Points.setText(String.valueOf(tournamentPositions.get(2).getPoints()));
        txtViewPosition4Points.setText(String.valueOf(tournamentPositions.get(3).getPoints()));

        txtViewPosition1Goals.setText(String.valueOf(tournamentPositions.get(0).getTournamentGoals()));
        txtViewPosition2Goals.setText(String.valueOf(tournamentPositions.get(1).getTournamentGoals()));
        txtViewPosition3Goals.setText(String.valueOf(tournamentPositions.get(2).getTournamentGoals()));
        txtViewPosition4Goals.setText(String.valueOf(tournamentPositions.get(3).getTournamentGoals()));
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.btn_to_tournament_overview:
                mListener.onBackToHomeInteraction();
                break;

            default:
                break;
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
    public interface OnTournamentResultsListener {

        public void onBackToHomeInteraction();
    }

}
