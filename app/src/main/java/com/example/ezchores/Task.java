package com.example.ezchores;

import java.util.Date;

public class Task {
    // parameters
    private int point;
    private boolean done;
    private Date date = null;
    private String name;

    public Task(int point, boolean done, Date date, String name) {
        this.point = point;
        this.done = done;
        this.date = date;
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}
