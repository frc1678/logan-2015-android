package com.example.sam.loganapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import com.example.sam.loganapp.realm.Team;

public class TeamActivity extends Activity {
    public Integer teamNum;
    public int teamMountSpeed;
    public Boolean teamCanMount;
    public boolean teamRemoveMech;
    public int[] ids = {R.id.radioButton, R.id.radioButton2, R.id.radioButton3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamNum = getIntent().getIntExtra("teamNum", 0);
        teamMountSpeed = getIntent().getIntExtra("teamMountSpeed", 0);
        teamCanMount = getIntent().getBooleanExtra("teamCanMount", false);
        teamRemoveMech = getIntent().getBooleanExtra("teamRemoveMech", false);

        addListenerOnRatingBar();
        setupUIForTeam();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnRatingBar(){
        RatingBar ratingBar = (RatingBar)findViewById(R.id.willingRB);
        final TextView willingText = (TextView)findViewById(R.id.willingTV);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                willingText.setText(String.valueOf(rating));
            }
        });
    }


    public void submitClicked(View view){
        Switch mountabilityValueSwitch = (Switch)findViewById(R.id.canMount);
        Switch removeMechValueSwitch = (Switch)findViewById(R.id.asdf);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.mountSpeed);
        TextView teamNumberTextView = (TextView)findViewById(R.id.teamNumber);

        Intent finishIntent = new Intent();
        for (int i=0; i < ids.length; i++) {
            if (((RadioButton)findViewById(ids[i])).isChecked()) {
                finishIntent.putExtra("teamMountSpeed", i);
            }
        }
        finishIntent.putExtra("teamCanMount", mountabilityValueSwitch.isChecked());
        finishIntent.putExtra("teamRemoveMech", removeMechValueSwitch.isChecked());
        finishIntent.putExtra("teamNum", teamNumberTextView.getText());
        setResult(RESULT_OK, finishIntent);
        finish();
    }

    public void setupUIForTeam() {
        Switch mountabilityValueSwitch = (Switch)findViewById(R.id.canMount);
        Switch removeMechValueSwitch = (Switch)findViewById(R.id.asdf);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.mountSpeed);
        TextView teamNumberTextView = (TextView)findViewById(R.id.teamNumber);


        if (teamMountSpeed > 0 && teamMountSpeed < ids.length) {
            radioGroup.check(ids[teamMountSpeed]);
        }
        mountabilityValueSwitch.setChecked(teamCanMount);
        removeMechValueSwitch.setChecked(teamRemoveMech);
        teamNumberTextView.setText(teamNum.toString());
    }
}
