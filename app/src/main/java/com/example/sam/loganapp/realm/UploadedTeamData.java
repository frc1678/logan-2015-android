package com.example.sam.loganapp.realm;

import java.lang.String;
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
    private boolean canMountMechanism;
    private boolean rampable;
    private int mountingSpeed;
    private boolean mechRemove;

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

    public boolean isCanMountMechanism() {
        return canMountMechanism;
    }

    public void setCanMountMechanism(boolean canMountMechanism) {
        this.canMountMechanism = canMountMechanism;
    }

    public boolean isRampable() {
        return rampable;
    }

    public void setRampable(boolean rampable) {
        this.rampable = rampable;
    }

    public int getMountingSpeed() {
        return mountingSpeed;
    }

    public void setMountingSpeed(int mountingSpeed) {
        this.mountingSpeed = mountingSpeed;
    }

    public boolean isMechRemove() {
        return mechRemove;
    }

    public void setMechRemove(boolean mechRemove) {
        this.mechRemove = mechRemove;
    }
}
