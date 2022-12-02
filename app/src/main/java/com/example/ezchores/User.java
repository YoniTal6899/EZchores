package com.example.ezchores;

import java.util.ArrayList;

public class User {
    // parameters
   private  int id;
   private  String name;
   private  ArrayList<Group> groupList;
   private  ArrayList<Task> taskList;


    // constructor
    public User(String name) {
        this.name = name;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
