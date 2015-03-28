package com.example.sam.loganapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.example.sam.loganapp.realm.Competition;
import com.example.sam.loganapp.realm.Team;
import com.example.sam.loganapp.realm.UploadedTeamData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by citruscircuits on 3/28/15.
 */
public class Utils {
    public static int numFiles = 0;

    private static DbxPath getNewChangePacketPath(String competition, int team, String type) {
        String packetFileName = competition + "-" + team + "-" + type + "_logan" + "|" + System.currentTimeMillis() + ".json";
        DbxPath path = new DbxPath(Constants.DBX_CHANGE_PACKETS_PATH + packetFileName);
        return path;
    }

    public static <T> void saveChangePacket(Activity activity, String competition, int team, String type, T change) {
        try {
            String fileName = getNewChangePacketPath(competition, team, type).toString().replace("/", "`");

            Log.e("test", "the filename is " + fileName);

            FileOutputStream changeFileStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);

            JSONObject changePacket = new JSONObject();
            changePacket.put("class", "Team");
            changePacket.put("uniqueKey", Constants.NUMBER_PROPERTY);
            changePacket.put("uniqueValue", team);

            JSONObject jsonChange = new JSONObject();
            jsonChange.put("keyToChange", "uploadedData." + type);
            jsonChange.put("valueToChangeTo", change);

            JSONArray changes = new JSONArray();
            changes.put(0, jsonChange);

            changePacket.put("changes", changes);

            byte[] bytes = changePacket.toString().getBytes();
            ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);

            int b;

            while ((b = bytesStream.read()) != -1) {
                changeFileStream.write(b);
            }

            changeFileStream.close();

            numFiles++;
        } catch (DbxException dbxe) {
            Log.e("test", "Dbx messed up..." + dbxe.getMessage());
        } catch (JSONException je) {
            Log.e("test", "JSON messed up..." + je.getMessage());
        } catch (IOException ioe) {
            Log.e("test", "IO messed up..." + ioe.getMessage());
        }
    }

    public static String getCompetitionCode(Realm realm) {
        RealmQuery<Competition> firstTeamQuery = realm.where(Competition.class);
        return firstTeamQuery.findFirst().getCompetitionCode();
    }

    public static boolean uploadChangePacket(DbxFileSystem dbxFs, Activity activity, String fileName) {

        File file = new File(activity.getFilesDir(), fileName);

        DbxFile changeFile = null;
        boolean didSucceed = false;
        try {
            if (!file.getName().contains("realm")) {
                Log.e("test", "Uploading " + file.getName());
                fileName = fileName.replace("`", "/");

                changeFile = dbxFs.create(new DbxPath(fileName));

                changeFile.writeFromExistingFile(file, false);

                boolean didDelete = file.delete();

                if (didDelete) {
                    numFiles--;
                }

                Log.e("test", "Did delete file " + fileName);
            }
        } catch (IOException ioe) {
            Toast toast = Toast.makeText(activity.getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT);
            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setTextColor(Color.RED);
            toast.show();
            didSucceed = false;
        }

        if (changeFile != null) {
            changeFile.close();
        }

        return didSucceed;
    }

    public static Team[] getSortedTeams(Realm realm) {
        if (realm != null) {
            Log.e("test", "Realm is not null!");
            RealmResults<Team> teamsResults = realm.where(Team.class).findAll();

            Log.e("test", "Begin sort");
            teamsResults.sort("number");
            Log.e("Test", "Finished sorting by number");

            ArrayList<Team> sortedTeamsArrayList = new ArrayList<Team>(Arrays.asList((Team[])teamsResults.toArray()));

            Comparator isSetComparator = new Comparator() {
                @Override
                public int compare(Object lhs, Object rhs) {
                    Team teamOne = (Team)lhs;
                    Team teamTwo = (Team)rhs;

                    return boolToInt(teamOne.getUploadedData().getMountingWillingness() != -1) - boolToInt(teamTwo.getUploadedData().getMountingWillingness() != -1);
                }
            };
            Collections.sort(sortedTeamsArrayList, isSetComparator);
            Log.e("test", "Finished sorting by comparator");

            Team[] sortedTeams = (Team[])sortedTeamsArrayList.toArray();
            return sortedTeams;
        } else {
            return new Team[0];
        }
    }


    public static int boolToInt(boolean b) {
        return ( b ? 1 : 0 );
    }

}
