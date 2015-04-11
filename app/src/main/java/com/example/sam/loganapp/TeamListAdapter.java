package com.example.sam.loganapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sam.loganapp.realm.Team;

import java.util.ArrayList;

/**
 * Created by citruscircuits on 3/28/15.
 */
public class TeamListAdapter extends BaseAdapter {
    ArrayList<Team> teamsSearched = new ArrayList<Team>();
    Context context;

    public TeamListAdapter(ArrayList<Team> teamsSearched, Context context) {
        this.teamsSearched = teamsSearched;
        this.context = context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return teamsSearched.size();
    }

    @Override
    public Object getItem(int i) {
        Team team = teamsSearched.get(i);
        return team;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.team_list_cell, viewGroup, false);
        }

        TextView teamText = (TextView) rowView.findViewById(R.id.teamText);
        final Team team = (Team)getItem(i);
        teamText.setText(team.getNumber()+"");


        if (team.getUploadedData().getEaseOfMounting() != -1) {
            teamText.setTextColor(Color.BLACK);
        } else {
            teamText.setTextColor(Color.RED);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeamActivity.class);
                intent.putExtra("teamNum", team.getNumber());
                intent.putExtra("teamCanMount", team.getUploadedData().isCanMountMechanism());
                intent.putExtra("teamMountingSpeed", team.getUploadedData().getEaseOfMounting());
                intent.putExtra("teamWillingness", team.getUploadedData().isWillingToMount());
                ((Activity)context).startActivityForResult(intent, Constants.REQUEST_TEAM_ACTIVITY);
            }
        });

        return rowView;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
