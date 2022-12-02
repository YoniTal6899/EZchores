package com.example.ezchores;

import java.util.ArrayList;

public class Group {

    // parameters
    private ArrayList<User> groupList;
    private ArrayList<Task> taskList;
    private String name;
    private int groupSize = 0;
    private int taskSize = 0;
    private ArrayList<User> adminList;

    // constructor
    public Group(ArrayList<User> groupList, ArrayList<Task> taskList, String name , User admin) {
        this.groupList = groupList;
        this.taskList = taskList;
        this.name = name;
        this.adminList.add(admin);
    }

    public ArrayList<User> getGroupList() {
        return groupList;
    }

    public void addUser(User newUser){
        this.groupList.add(newUser);
    }

    public void removeUser(User user){
        this.groupList.remove(groupList.indexOf(user));
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void addTaskList(Task newTask) {
        this.taskList.add(newTask);
    }

    public void removeTask(Task task){
        this.taskList.remove(taskList.indexOf(task));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }
}




