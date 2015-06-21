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
    TeamListActivity teamListActivity;
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
        teamListActivity = (TeamListActivity) params[1];

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
            if (teamListActivity.realm != null) {
                teamListActivity.realm.close();
            }
            Realm.deleteRealmFile(teamListActivity, "realm.realm");
            OutputStream outputStream = teamListActivity.openFileOutput("realm.realm", Context.MODE_PRIVATE);
            int b = 0;
            Log.e("test", "Time to start reading and writing!");
            while ((b = realmStream.read()) != -1) {
                outputStream.write(b);
            }
            for (String file : teamListActivity.fileList()) {
                Log.e("test", "The file is " + file);
            }
            Log.e("test", "File size is " + teamListActivity.openFileInput("realm.realm").available());
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

        }
        try {
            teamListActivity.realm = Realm.getInstance(teamListActivity.getApplicationContext(), "realm.realm");
            teamListActivity.realm.refresh();
            teamListActivity.resetListView();
        } catch (Exception e) {
            teamListActivity.realm = null;
        }
    }
}

