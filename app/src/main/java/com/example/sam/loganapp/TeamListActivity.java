package com.example.sam.loganapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileSystem;
import com.example.sam.loganapp.realm.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import io.realm.Realm;


public class TeamListActivity extends ListActivity {
    public DbxFileSystem dbxFs;
    public Realm realm;
    private DbxAccountManager DAM;
    Team[] teams = new Team[0];
    ArrayList<Team> teamsSearched = new ArrayList<Team>();
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        context = getApplicationContext();

        DAM = DbxAccountManager.getInstance(context, Constants.APP_KEY, Constants.APP_SECRET);
        if (!DAM.hasLinkedAccount()) {
            DAM.startLink(this, Constants.REQUEST_LINK_TO_DBX);
        } else {
            afterDbxLink();
        }

        setupList();
    }

    public void setupList() {
        EditText searchText = (EditText)findViewById(R.id.searchBar);
        searchText.setHint("Search teams...");
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchWithText(s.toString());
            }
        });

        resetListView();
    }

    public void resetListView() {
        Log.e("test", "Resetting list view.");
        Log.e("test", "The teams are " + Utils.getSortedTeams(realm));
        teams = Utils.getSortedTeams(realm);
        searchWithText("");
        resetListAdapter();
    }

    public void resetListAdapter() {
        setListAdapter(new BaseAdapter() {

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
                Team team = (Team)getItem(i);
                teamText.setText(team.getNumber());

                if (team.getUploadedData().getMountingWillingness() != -1) {
                    teamText.setTextColor(Color.BLACK);
                } else {
                    teamText.setTextColor(Color.RED);
                }

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
        });
    }

    public void searchWithText(String searchString) {
        teamsSearched.clear();

        teamsSearched = new ArrayList<>();

        for (Team team : teams) {
            if ((team.getNumber() + "").startsWith(searchString) || searchString.isEmpty()) {
                teamsSearched.add(team);
            }
        }

        resetListAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. T
        // he action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.uploadToDbx) {
            uploadAllChangePackets();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadAllChangePackets() {
        String[] files = this.fileList();

        for (String file : files) {
            if (RealmFileUploadTask.getActiveThreadCount() < Utils.numFiles) {
                new RealmFileUploadTask().execute(dbxFs, this, file);
            }
        }
    }

    public void afterDbxLink() {
        try {
            dbxFs = DbxFileSystem.forAccount(DAM.getLinkedAccount());
            dbxFs.awaitFirstSync();
        } catch (DbxException e) {
            Toast toast = Toast.makeText(this, "ERROR! TRY AGAIN!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        new RealmDownloadTask().execute(dbxFs, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView teamNumberView = (TextView) v.findViewById(R.id.teamText);
        String teamNumberText = teamNumberView.getText().toString();
        Integer selectedTeamNumber = Integer.parseInt(teamNumberText);
        Intent finishIntent = new Intent();
        finishIntent.putExtra("selectedTeam", selectedTeamNumber);
        setResult(Activity.RESULT_OK, finishIntent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                afterDbxLink();
            } else {
                DAM = DbxAccountManager.getInstance(getApplicationContext(), Constants.APP_KEY, Constants.APP_SECRET);
                DAM.startLink(this, Constants.REQUEST_LINK_TO_DBX);
            }
        } else if (requestCode == Constants.REQUEST_TEAM_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                float willingness = data.getFloatExtra("willingness", (float)0.0);
                boolean canMount = data.getBooleanExtra("canMount", false);

                Utils.saveChangePacket(this, Utils.getCompetitionCode(realm), Constants.currentTeamNumber, Constants.WILLINGNESS_TO_MOUNT_TYPE, willingness);
                Utils.saveChangePacket(this, Utils.getCompetitionCode(realm), Constants.currentTeamNumber, Constants.CAN_MOUNT_TYPE, canMount);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
