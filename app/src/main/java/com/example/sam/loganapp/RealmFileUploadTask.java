package com.example.sam.loganapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Looper;

import com.dropbox.sync.android.DbxFileSystem;

/**
 * Created by citruscircuits on 3/28/15.
 */
public class RealmFileUploadTask extends AsyncTask {
    private static int activeThreadCount = 0;

    public static int getActiveThreadCount() {
        return activeThreadCount;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        activeThreadCount++;

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        DbxFileSystem dbxFs = (DbxFileSystem)objects[0];
        Activity activity = (Activity)objects[1];
        String fileName = (String)objects[2];

        Utils.uploadChangePacket(dbxFs, activity, fileName);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        activeThreadCount--;
    }
}
