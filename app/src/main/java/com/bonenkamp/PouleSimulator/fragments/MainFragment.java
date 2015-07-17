package com.bonenkamp.PouleSimulator.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.activities.TournamentSimulationActivity;
import com.bonenkamp.PouleSimulator.data.DatabaseHelper;
import com.bonenkamp.PouleSimulator.data.LoadTeams;
import com.bonenkamp.PouleSimulator.data.RefereeXmlParser;
import com.bonenkamp.PouleSimulator.data.TournamentDatabaseContract;
import com.bonenkamp.PouleSimulator.data.TeamsXmlParser;
import com.bonenkamp.PouleSimulator.models.Team;
import com.bonenkamp.PouleSimulator.models.XmlRefereeEntry;
import com.bonenkamp.PouleSimulator.models.XmlTeamEntry;

import android.database.SQLException;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnCreateTeamListener} interface
 * to handle interaction events.
 *
 * Fragment shown when app is launched.
 */
public class MainFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnCreateTeamListener onMainFragmentListener;

    private static final String PREFERENCE_STARTUP      = "isFirstStartUp";
    private static final String PREFERENCE_HAS_POULE    = "pool";
    private static final String TEAMS_XML_FILE_NAME     = "XML/teams.xml";
    private static final String REFEREE_XML_FILE_NAME   = "XML/referees.xml";

    private ArrayList<Team> teamsInPool = new ArrayList<>();

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;

    // Required empty public constructor
    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnStartTournament = (Button) rootView.findViewById(R.id.btnCreateTournament);
        Button btnCreateTeam = (Button) rootView.findViewById(R.id.btnCreateTeam);
        Button btnTeamStats = (Button) rootView.findViewById(R.id.btnTeamStats);

        btnStartTournament.setOnClickListener(this);
        btnCreateTeam.setOnClickListener(this);
        btnTeamStats.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isFirstStart()){
            // first time, create database and store 4 teams & info
            // calls loadData when finished
            new LoadDataTask().execute();
        }else{
            setUpPoule();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onMainFragmentListener = (OnCreateTeamListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onMainFragmentListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateTournament:
                //
                teamsInPool = getTeamsInPool();
                if (teamsInPool != null) {
                    //start new activity for tournament simulation
                    Intent intent = new Intent(getActivity(), TournamentSimulationActivity.class);
                    intent.putParcelableArrayListExtra("teams", teamsInPool);
                    startActivity(intent);
                }else {
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setTitle("Each team can only be once in the pool");
                    dialog.show();
                }

                break;

            case R.id.btnCreateTeam:
                onMainFragmentListener.onCreateTeam();
                break;

            case R.id.btnTeamStats:
                onMainFragmentListener.onViewTeamStats();

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
     * Check if it is the first time the app starts up
     */
    private boolean isFirstStart() {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences(
                getString(R.string.preference_file_start),
                Context.MODE_PRIVATE);

        return preferences.getBoolean(PREFERENCE_STARTUP, true);
    }

    /**
     * Setup the poule. If none exists we need to create a new one, otherwise load a saved poule.
     */
    private void setUpPoule() {

        ArrayList<Team> allTeams;

        // Load all teams from database
        allTeams = LoadTeams.getAllTeamsFromDatabase(getActivity());

        // Setup tournament views.
        setViews(allTeams);
    }

    /**
     * Set the TextViews in the UI with the Teamnames from the poule
     *
     * @param poule {@link List} with {@link Team} objects
     */
    private void setViews(ArrayList<Team> poule) {

        spinner1 = (Spinner) getView().findViewById(R.id.select_team_1);
        spinner2 = (Spinner) getView().findViewById(R.id.select_team_2);
        spinner3 = (Spinner) getView().findViewById(R.id.select_team_3);
        spinner4 = (Spinner) getView().findViewById(R.id.select_team_4);

        ArrayAdapter<Team> adapter = new ArrayAdapter<Team>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                poule);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);
        spinner4.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);
        spinner4.setOnItemSelectedListener(this);

        if(poule.size() > 3) {
            spinner1.setSelection(0);
            spinner2.setSelection(1);
            spinner3.setSelection(2);
            spinner4.setSelection(3);
        }

    }

    /**
     * Get all the selected teams in the spinners, and add the to an ArrayList.
     * Returns null if a duplicate is found
     * @return ArryList with the teams in the pool, null if team duplicates are found.
     */
    private ArrayList<Team> getTeamsInPool() {
        Team team1 = (Team) spinner1.getSelectedItem();
        Team team2 = (Team) spinner2.getSelectedItem();
        Team team3 = (Team) spinner3.getSelectedItem();
        Team team4 = (Team) spinner4.getSelectedItem();

        ArrayList<Team> arrayList = new ArrayList<>();

        arrayList.add(team1);
        arrayList.add(team2);
        arrayList.add(team3);
        arrayList.add(team4);

        for (int i = 0; i < arrayList.size(); i++ ) {
            for (int j = i + 1; j < arrayList.size(); j++ ) {
                if (arrayList.get(i) == arrayList.get(j)) {
                    return null;
                }
            }
        }

        return arrayList;
    }

    /**
     * Create Database en store the needed team information
     * with an aSyncTask for background processing
     *
     * @see {@link AsyncTask}
     */
    private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // create and show loading dialog
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(R.string.progressdialog_title);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //Get the XML data as Collection of XmlTeamEntry
            List<XmlTeamEntry> teamEntries          = null;
            List<XmlRefereeEntry> refereeEntries    = null;

            TeamsXmlParser teamsXmlParser       = new TeamsXmlParser();
            RefereeXmlParser refereeXmlParser   = new RefereeXmlParser();

            // Get writable database instance, if it is the first it is called,
            // the database will be created by a call to onCreate.
            DatabaseHelper helper = new DatabaseHelper(getActivity());
            SQLiteDatabase database = helper.getWritableDatabase();


            try{
                //Get the xml file, teams.xml, from the assets folder
                InputStream teamsStream = getActivity().getAssets().open(TEAMS_XML_FILE_NAME);

                //Get the xml file, referee.xml, from the assets folder
                InputStream refereeStream = getActivity().getAssets().open(REFEREE_XML_FILE_NAME);

                //parse team XML file
                teamEntries = teamsXmlParser.parse(teamsStream);

                //parse referee XML file
                refereeEntries = refereeXmlParser.parse(refereeStream);

                // Close the stream
                teamsStream.close();
                refereeStream.close();
            }catch (IOException e) {
                Log.e("Inputstream XML file", e.toString());
            }catch (XmlPullParserException e){
                Log.e("Error in TeamsXmlParser", e.toString());
            }

            // Iterate through referee entries, and add them with the corresponding
            // column in a collection
            for(XmlTeamEntry entry : teamEntries) {
                ContentValues  values = new ContentValues();
                values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_NAME, entry.name);
                values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_ATT_STRENGTH, entry.att);
                values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_MID_STRENGTH, entry.mid);
                values.put(TournamentDatabaseContract.TeamTable.KEY_TEAM_DEF_STRENGTH, entry.def);

                try{
                    database.insertOrThrow(
                            TournamentDatabaseContract.TeamTable.TEAMS_TABLE_NAME,
                            null,
                            values);
                }catch (SQLException e){
                    Log.e("SQLException Insert", e.toString());
                }
            }

            // Iterate through referee entries, and add them with the corresponding
            // column in a collection
            for(XmlRefereeEntry entryReferee : refereeEntries) {
                ContentValues  values = new ContentValues();
                values.put(TournamentDatabaseContract.RefereeTable.KEY_REFEREE_NAME, entryReferee.name);
                values.put(TournamentDatabaseContract.RefereeTable.KEY_REFEREE_STRICTNESS,
                        entryReferee.strictness);

                try{
                    database.insertOrThrow(
                            TournamentDatabaseContract.RefereeTable.REFEREE_TABLE_NAME,
                            null,
                            values);
                }catch (SQLException e){
                    Log.e("SQLException Insert", e.toString());
                }
            }
            database.close();
            helper.close();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if(result) {
                //Change preference setting 'isFirstStartUp' to false
                Context context = getActivity();
                SharedPreferences preferences = context.getSharedPreferences(
                                                getString(R.string.preference_file_start),
                                                Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREFERENCE_STARTUP, false);
                editor.commit();

                // Set up Poule
                setUpPoule();

            }
            //hide loading dialog
            dialog.dismiss();
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
    public interface OnCreateTeamListener {
        public void onCreateTeam();

        public void onViewTeamStats();
    }
}
