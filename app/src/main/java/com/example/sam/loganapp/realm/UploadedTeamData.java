package com.example.sam.loganapp.realm;

import java.lang.reflect.Array;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by Citrus Circuits on 10/15/2014.
 */

@RealmClass
public class UploadedTeamData extends RealmObject {
    private String pitOrganization;
    private String programmingLanguage;
    private String pitNotes;
    private float mountingWillingness;
    private boolean canMountMechanism;

    public String getPitOrganization() {
        return pitOrganization;
    }

    public void setPitOrganization(String pitOrganization) {
        this.pitOrganization = pitOrganization;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getPitNotes() {
        return pitNotes;
    }

    public void setPitNotes(String pitNotes) {
        this.pitNotes = pitNotes;
    }

    public float getMountingWillingness() {
        return mountingWillingness;
    }

    public void setMountingWillingness(float mountingWillingness) {
        this.mountingWillingness = mountingWillingness;
    }

    public boolean isCanMountMechanism() {
        return canMountMechanism;
    }

    public void setCanMountMechanism(boolean canMountMechanism) {
        this.canMountMechanism = canMountMechanism;
    }
}
