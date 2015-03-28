package com.example.sam.loganapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.example.sam.loganapp.realm.Team;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.processor.Utils;

/**
 * Created by citruscircuits on 2/15/15.
 */


public class RealmDownloadTask extends AsyncTask {
    MainActivity mainActivity;
    private static int activeThreadCount = 0;
    DbxFile realmFile;
    FileInputStream fis;

    public static int getActiveThreadCount() {
        return activeThreadCount;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        activeThreadCount++;
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        DbxFileSystem dbxFs = (DbxFileSystem) params[0];
        mainActivity = (MainActivity) params[1];
        try {
            dbxFs.setMaxFileCacheSize(0);
            dbxFs.setMaxFileCacheSize(Constants.DEFAULT_CACHE_SIZE);
            DbxPath realmPath = new DbxPath(Constants.DBX_REALM_DB_PATH);
            Log.e("test", "The database path is " + realmPath);
            realmFile = dbxFs.open(realmPath);
            try {
                Log.e("test", "Time to get readStream");
                fis = realmFile.getReadStream();
                Log.e("test", "Finished getting readStream");
            } catch (IOException ioe) {
//                Toaster.makeErrorToast(ioe.getMessage(), Toast.LENGTH_LONG);
            }
        } catch (DbxException e) {
//            Toaster.makeErrorToast(e.getMessage(), Toast.LENGTH_LONG);
            Log.e("test", "The error message is " + e.getMessage());
        }
        return fis;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        activeThreadCount--;
        InputStream realmStream = (InputStream) o;
        try {
            if (mainActivity.realm != null) {
                mainActivity.realm.close();
            }
            Realm.deleteRealmFile(mainActivity, "realm.realm");
            OutputStream outputStream = mainActivity.openFileOutput("realm.realm", Context.MODE_PRIVATE);
            int b = 0;
            Log.e("test", "Time to start reading and writing!");
            while ((b = realmStream.read()) != -1) {
// Log.e("test", "read! " + b);
                outputStream.write(b);
            }
            for (String file : mainActivity.fileList()) {
                Log.e("test", "The file is " + file);
            }
            Log.e("test", "File size is " + mainActivity.openFileInput("realm.realm").available());
            outputStream.close();
        } catch (IOException ioe) {
            Log.e("Test", "ERROR " + ioe.getMessage());
        } catch (NullPointerException npe) {
            Log.e("test", "File empty");
        }
        try {
            realmStream.close();
            realmFile.close();
        } catch (IOException ioe) {
            realmFile.close();
        } catch (NullPointerException npe) {
//Do nothing
        }
        Log.e("test", "Setting new Realm");
        Team team = null;
        try {
            mainActivity.realm = Realm.getInstance(mainActivity.getApplicationContext(), "realm.realm");
            RealmQuery<Team> teamRealmQuery = mainActivity.realm.where(Team.class);
            teamRealmQuery.equalTo("number", Constants.currentTeamNumber);
            team = teamRealmQuery.findFirst();
            mainActivity.realm.refresh();
        } catch (Exception e) {
            mainActivity.realm = null;
        }
        if (team != null) {
            mainActivity.team = team;
        } else {
            mainActivity.getFirstTeam();
        }
        Log.e("test", "Current team number is " + Constants.currentTeamNumber);
//        if (Constants.currentTeamNumber == -1) {
//            mainActivity.updateTeam(false);
//        } else {
//            mainActivity.updateTeam(true);
//        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.e("test", "CANCELLED THREAD!");
        try {
            fis.close();
            Log.e("test", "Closed.");
        } catch (IOException ios) {
            Log.e("test", "Could not close!");
        }
        realmFile.close();
    }
}

