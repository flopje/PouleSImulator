package com.bonenkamp.PouleSimulator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bonenkamp.PouleSimulator.R;
import com.bonenkamp.PouleSimulator.models.Team;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * An extension to ArrayAdapter, to allow for cutom views in the
 * {@link com.bonenkamp.PouleSimulator.fragments.TeamsInfoFragment}
 */
public class TeamsAdapter extends ArrayAdapter<Team> {

    private static class ViewHolder {
        TextView teamName;
        TextView teamAverage;
        TextView teamAttackStrength;
        TextView teamMidfieldStrength;
        TextView teamDefenseStrength;

    }

    public TeamsAdapter(Context context, ArrayList<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Team team = getItem(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_team, parent, false);

            viewHolder.teamName = (TextView) convertView.findViewById(R.id.item_team_name);
            viewHolder.teamAverage = (TextView) convertView.findViewById(R.id.item_average_goals);
            viewHolder.teamAttackStrength = (TextView) convertView.findViewById(R.id.item_attack_stat);
            viewHolder.teamMidfieldStrength = (TextView) convertView.findViewById(R.id.item_midfield_stat);
            viewHolder.teamDefenseStrength = (TextView) convertView.findViewById(R.id.item_defense_stat);

            convertView.setTag(R.string.viewHolderId, viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag(R.string.viewHolderId);
        }
        viewHolder.teamName.setText(team.name());
        viewHolder.teamAverage.setText(String.valueOf(team.averageGoals()));
        viewHolder.teamAttackStrength.setText(String.valueOf(team.attackStrength()));
        viewHolder.teamMidfieldStrength.setText(String.valueOf(team.midFieldStrength()));
        viewHolder.teamDefenseStrength.setText(String.valueOf(team.defenseStrength()));

        convertView.setTag(R.string.listObjectId, team.id());


        return convertView;
    }
}
