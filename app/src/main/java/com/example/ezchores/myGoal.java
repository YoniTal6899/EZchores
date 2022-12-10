package com.example.ezchores;

import java.util.Date;

public class myGoal {
    // parameters
    private int point;
    private String name,assignID;

    public myGoal(int point, String name,String assignID) {
        this.point = point;
        this.name = name;
        this.assignID = assignID;
    }
    public myGoal(){};
    public String getAssignID() {
        return assignID;
    }

    public void setAssignID(String assignID) {
        this.assignID = assignID;
    }


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
