package com.example.ezchores;

import java.util.ArrayList;

public class User {
    // parameters

   private  String id;
   private  String name;
   private String email;
   private String password;
   public   ArrayList<Group> groupList;
   public  ArrayList<myTask> taskList;
   private  int curr_points;




    // constructor
    public User(String name,String email,String password) {

        this.name = name;
        this.email=email;
        this.password=password;
        this.curr_points = 0;

    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public int getCurr_points() {
        return curr_points;
    }

    public void setCurr_points(int curr_points) {
        this.curr_points = curr_points;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getId() {return id;}

    public void setId(String id) {

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


    public ArrayList<myTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(myTask taskList) {
        this.taskList.add(taskList);

    }
}
