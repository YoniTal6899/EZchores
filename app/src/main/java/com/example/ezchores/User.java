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




    // constructor
    public User(String name,String email,String password) {

        this.name = name;
        this.email=email;
        this.password=password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
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
