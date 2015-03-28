package com.example.sam.loganapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
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
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import com.example.sam.loganapp.realm.Team;

public class TeamActivity extends ActionBarActivity {
    public Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        TextView willingnessValueTextView = (TextView)findViewById(R.id.willingTV);
        ToggleButton mountabilityValueToggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        Intent finishIntent = new Intent();
        finishIntent.putExtra("willingness", Float.parseFloat(willingnessValueTextView.getText().toString()));
        finishIntent.putExtra("canMount", mountabilityValueToggleButton.isChecked());
        setResult(RESULT_OK, finishIntent);
        finish();
    }

    public void setupUIForTeam() {
        RatingBar willingnessValueRatingBar = (RatingBar)findViewById(R.id.willingRB);
        ToggleButton mountabilityValueToggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        willingnessValueRatingBar.setRating(team.getUploadedData().getMountingWillingness());
        mountabilityValueToggleButton.setChecked(team.getUploadedData().isCanMountMechanism());
    }
}