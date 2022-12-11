package com.example.ezchores;

import java.util.ArrayList;

public class Group {

    // parameters
    private ArrayList<User> groupList;

    private ArrayList<myTask> taskList;
    private String name;
    private String admin_id;

    // constructor
    public Group( String name ) {
        this.name = name;
    }
    public Group(){};



    public ArrayList<User> getGroupList() {
        return groupList;
    }

    public void addUser(User newUser){
        this.groupList.add(newUser);
    }

    public void removeUser(User user){
        this.groupList.remove(groupList.indexOf(user));
    }


    public ArrayList<myTask> getTaskList() {
        return taskList;
    }

    public void addTaskList(myTask newTask) {
        this.taskList.add(newTask);
    }

    public void removeTask(myTask task){

        this.taskList.remove(taskList.indexOf(task));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

}




