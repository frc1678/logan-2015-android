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
    private int numWheels;
    private int numMotors;
    private int maxPitStackHeight;
    private String pitOrganization;
    private String drivetrain;
    private String typesWheels;
    private String programmingLanguage;
    private int weight;
    private String pitNotes;
    private int withholdingAllowanceUsed;
    private int mountingWillingness;
    private int easeOfMountability;
    private boolean hasUpdatedSoftware;
    private int numberOfProgrammers;

    public int getNumMotors() {
        return numMotors;
    }

    public void setNumMotors(int numMotors) {
        this.numMotors = numMotors;
    }

    public String getPitOrganization() {
        return pitOrganization;
    }

    public void setPitOrganization(String pitOrganization) {
        this.pitOrganization = pitOrganization;
    }

    public int getMaxPitStackHeight() {
        return maxPitStackHeight;
    }

    public void setMaxPitStackHeight(int maxPitStackHeight) {
        this.maxPitStackHeight = maxPitStackHeight;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(String drivetrain) {
        this.drivetrain = drivetrain;
    }

    public int getNumWheels() {
        return numWheels;
    }

    public void setNumWheels(int numWheels) {
        this.numWheels = numWheels;
    }

    public String getTypesWheels() {
        return typesWheels;
    }

    public void setTypesWheels(String typesWheels) {
        this.typesWheels = typesWheels;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPitNotes() {
        return pitNotes;
    }

    public void setPitNotes(String pitNotes) {
        this.pitNotes = pitNotes;
    }

    public int getWithholdingAllowanceUsed() {
        return withholdingAllowanceUsed;
    }

    public void setWithholdingAllowanceUsed(int withholdingAllowanceUsed) {
        this.withholdingAllowanceUsed = withholdingAllowanceUsed;
    }

    public int getMountingWillingness() {
        return mountingWillingness;
    }

    public void setMountingWillingness(int mountingWillingness) {
        this.mountingWillingness = mountingWillingness;
    }

    public int getEaseOfMountability() {
        return easeOfMountability;
    }

    public void setEaseOfMountability(int easeOfMountability) {
        this.easeOfMountability = easeOfMountability;
    }

    public boolean isHasUpdatedSoftware() {
        return hasUpdatedSoftware;
    }

    public void setHasUpdatedSoftware(boolean hasUpdatedSoftware) {
        this.hasUpdatedSoftware = hasUpdatedSoftware;
    }

    public int getNumberOfProgrammers() {
        return numberOfProgrammers;
    }

    public void setNumberOfProgrammers(int numberOfProgrammers) {
        this.numberOfProgrammers = numberOfProgrammers;
    }
}
