package com.bonenkamp.PouleSimulator.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.data.SaveCustomTeam;
import com.bonenkamp.PouleSimulator.helpers.InputFilterMinMax;
import com.bonenkamp.PouleSimulator.models.Team;

/**
 * The Create Team Fragment. Shows the view in whcich a user can create his or her own team
 * and saves it in the database
 * <p>
 * Fragment containing for creating a new Team.
 */
public class CreateTeamFragment extends Fragment implements View.OnClickListener {

    private String teamName;
    private int attackStrength;
    private int midfieldStrength;
    private int defenseStrength;

    EditText etxtTeamName;
    EditText etxtAttackStrength;
    EditText etxtMidfieldStrength;
    EditText etxtDefenseStrength;


    public CreateTeamFragment() {
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
        return inflater.inflate(R.layout.fragment_create_team, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btnCancel = (Button) getView().findViewById(R.id.btn_cancel_create_team);
        Button btnCreate =  (Button) getView().findViewById(R.id.btn_confirm_team);

        btnCancel.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        etxtTeamName           = (EditText) getView().findViewById(
                R.id.input_team_name);
        etxtAttackStrength     = (EditText) getView().findViewById(
                R.id.input_attack_strength);
        etxtMidfieldStrength   = (EditText) getView().findViewById(
                R.id.input_midfield_strength);
        etxtDefenseStrength    = (EditText) getView().findViewById(
                R.id.input_defense_strength);

        etxtAttackStrength.setFilters(new InputFilter[] { new InputFilterMinMax(1, 100)});
        etxtMidfieldStrength.setFilters(new InputFilter[] { new InputFilterMinMax(1, 100)});
        etxtDefenseStrength.setFilters(new InputFilter[] { new InputFilterMinMax(1, 100)});
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_team:
                if (saveNewTeam()) {
                    getFragmentManager().popBackStack();
                }else {
                    Dialog alertDialog = new Dialog(getActivity());
                    alertDialog.setTitle("Something went wrong, please try again.");
                    alertDialog.setCancelable(true);
                }
                break;

            case R.id.btn_cancel_create_team:
                getFragmentManager().popBackStack();
                break;

            default:
                break;
        }
    }

    private boolean saveNewTeam() {

        if(setTeamParameters()) {
            return new SaveCustomTeam().saveTeamInDatabase(teamName, attackStrength,
                    midfieldStrength, defenseStrength, getActivity());
        }

        return false;

    }

    private boolean setTeamParameters() {

        teamName = etxtTeamName.getText().toString();

        try {
            attackStrength      = Integer.valueOf(etxtAttackStrength.getText().toString());
            midfieldStrength    = Integer.valueOf(etxtMidfieldStrength.getText().toString());
            defenseStrength     = Integer.valueOf(etxtDefenseStrength.getText().toString());
        }catch (NumberFormatException  e) {
            Log.e("Incorrect values", e.toString());
            return false;
        }

        return true;

    }

}
