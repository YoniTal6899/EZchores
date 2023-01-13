package com.example.ezchores;

import java.util.Date;

public class myTask {
    // parameters
    private int point;
    private String name,comment,assignID;
    private boolean isComplete;

    public myTask(int point, String name ,String assignID,boolean comp) {
        this.point = point;
        this.name = name;
        this.assignID = assignID;
        this.isComplete=comp;
    }
    public myTask(){};

    public boolean getIsComplete(){return this.isComplete;}

    public void setIsComplete(boolean b){this.isComplete=b;}

    public String getAssignID() {
        return assignID;
    }

    public void setAssignID(String assignID) {
        this.assignID = assignID;
    }

    public String getComment() {return comment;}

    public void setComment(String comment) {this.comment = comment;}

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}
