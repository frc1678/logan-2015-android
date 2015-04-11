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
    public float teamMountSpeed;
    public Boolean teamCanMount;
    public boolean teamIsWilling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamNum = getIntent().getIntExtra("teamNum", 0);
        teamMountSpeed = getIntent().getFloatExtra("teamMountingSpeed", 0);
        teamCanMount = getIntent().getBooleanExtra("teamCanMount", false);
        teamIsWilling = getIntent().getBooleanExtra("teamWillingness", false);

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
        TextView willingnessValueTextView = (TextView)findViewById(R.id.willingTV);
        TextView teamNumberTextView = (TextView)findViewById(R.id.teamNumber);


        Intent finishIntent = new Intent();

        finishIntent.putExtra("teamCanMount", mountabilityValueSwitch.isChecked());
        finishIntent.putExtra("teamWillingness", removeMechValueSwitch.isChecked());
        finishIntent.putExtra("teamMountingSpeed", Float.parseFloat(willingnessValueTextView.getText().toString()));
        finishIntent.putExtra("teamNum", teamNumberTextView.getText());
        setResult(RESULT_OK, finishIntent);
        finish();
    }

    public void setupUIForTeam() {
        Switch mountabilityValueSwitch = (Switch)findViewById(R.id.canMount);
        RatingBar mountSpeedRatingBar = (RatingBar)findViewById(R.id.willingRB);
        Switch willingValueSwitch = (Switch)findViewById(R.id.asdf);
        TextView teamNumberTextView = (TextView)findViewById(R.id.teamNumber);

        mountabilityValueSwitch.setChecked(teamCanMount);
        mountSpeedRatingBar.setRating(teamMountSpeed);
        willingValueSwitch.setChecked(teamIsWilling);
        teamNumberTextView.setText(teamNum.toString());
    }
}
