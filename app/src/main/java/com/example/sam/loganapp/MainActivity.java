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

public class MainActivity extends ActionBarActivity {
    private DbxAccountManager DAM;
    public static final String APP_KEY = "fu1drprr1bha4zl";
    public static final String APP_SECRET = "x8f4ehb2qyk30r4";
    static final int REQUEST_LINK_TO_DBX = 0;
    public DbxFileSystem dbxFs;
    public Realm realm;
    public Team team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DAM = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
            if (!DAM.hasLinkedAccount()) {
                DAM.startLink(this, REQUEST_LINK_TO_DBX);
                Log.e("asdf", "asf");

            } else {
                afterDbxLink();
            }

            addListenerOnRatingBar1();

        }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnRatingBar1(){
        RatingBar ratingBar = (RatingBar)findViewById(R.id.willingRB);
        final TextView willingText = (TextView)findViewById(R.id.willingTV);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                willingText.setText(String.valueOf(rating));
            }
        });
    }


    // Comments
    public void submitClicked(View view){
      TextView willingnessValue = (TextView)findViewById(R.id.willingTV);
      ToggleButton mountabilityValue = (ToggleButton)findViewById(R.id.toggleButton);
      String TeamValue = "Class: Team + " +
                          "Team# = 1678" +
                          willingnessValue.getText() +
                          Boolean.toString(mountabilityValue.isChecked());
      uploadChangePackets("" + TeamValue, "cheesecakeValues.txt");

}


    public void afterDbxLink() {
        try {
            dbxFs = DbxFileSystem.forAccount(DAM.getLinkedAccount());
        } catch (DbxException e) {
            Context context = this.getApplicationContext();
            CharSequence warning = "ERROR! TRY AGAIN!";
            int Duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, warning, Duration);
            toast.show();
            e.printStackTrace();
        }

        new RealmDownloadTask().execute(dbxFs, this);

    }

    public Team getFirstTeam() {
//        RealmQuery<Team> query = realm.where(Team.class);
//        Team team = query.findFirst();
       // Log.e("asdF", team.toString());
  //      return team;
        return null;
    }

//    public void updateUI() {
//        title.setText(team.getNumber());
//    }
    public void uploadChangePackets(String data, String fileName) {
        DbxPath dbxP = new DbxPath("/Change Packets/Unprocessed/" + fileName);
        try {
            DbxFile dbxF = dbxFs.create(dbxP);
            dbxF.writeString(data);
            dbxF.close();

        } catch (DbxException e) {
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                afterDbxLink();
            } else {
                DAM = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
                DAM.startLink(this, REQUEST_LINK_TO_DBX);

                // ... Link failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
