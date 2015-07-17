package com.bonenkamp.PouleSimulator.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.adapters.TeamsAdapter;
import com.bonenkamp.PouleSimulator.data.LoadTeams;
import com.bonenkamp.PouleSimulator.data.RemoveRecordFromDatabase;
import com.bonenkamp.PouleSimulator.data.TournamentDatabaseContract;
import com.bonenkamp.PouleSimulator.models.Team;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} fragment.
 * Show a listview containing all the teams and some information about them. Onclick of an item will
 * prompt the user to ask them if the want to delete the team.
 */
public class TeamsInfoFragment extends Fragment {

    private TeamsAdapter teamsAdapter;


    public TeamsInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teams_info, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ListView listView = (ListView) getView().findViewById(R.id.listView_teams);

        ArrayList<Team> allTeams = LoadTeams.getAllTeamsFromDatabase(getActivity());

        teamsAdapter = new TeamsAdapter(getActivity(), allTeams);

        // Add listview item onclicklistener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final View view2 = view;

                // Crate a dialog for confirmation about their action to delete a Team.
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Delete?");
                alertDialog.setMessage("Are you sure you want to delete "
                        + teamsAdapter.getItem(position).name()
                        + "?");


                alertDialog.setNegativeButton("Cancel", null);

                alertDialog.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Delete team
                        new RemoveRecordFromDatabase().removeRecordFromDatabase(
                                TournamentDatabaseContract.TeamTable.TEAMS_TABLE_NAME,
                                (int) view2.getTag(R.string.listObjectId),
                                getActivity());

                        teamsAdapter.remove(teamsAdapter.getItem(position));
                        teamsAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();


            }
        });

        listView.setAdapter(teamsAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
