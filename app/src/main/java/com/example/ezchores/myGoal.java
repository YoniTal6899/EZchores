package com.example.ezchores;

import java.util.Date;

public class myGoal {
    // parameters
    private int value,currentPoints;
    private String name,assignID;
    private boolean isComplete;

    public myGoal(int point, String name,String assignID,boolean comp) {
        this.value = point;
        this.name = name;
        this.assignID = assignID;
        this.currentPoints = 0;
        this.isComplete=comp;
    }
    public myGoal(){};

    public boolean getIsComplete(){return this.isComplete;}

    public void setIsComplete(boolean b){this.isComplete=b;}

    public String getAssignID() {
        return assignID;
    }

    public void setAssignID(String assignID) {
        this.assignID = assignID;
    }

    public int getCurrentPoints() {return currentPoints;}

    public void setCurrentPoints(int currentPoints) {this.currentPoints = currentPoints;}

    public int getValue() {
        return value;
    }

    public void setValue(int point) {
        this.value = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}
