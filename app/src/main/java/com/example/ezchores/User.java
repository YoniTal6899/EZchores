package com.example.ezchores;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {
    // parameters

   public String id;
   public String name;
   public String email;
   public String password;
   public ArrayList<Group> groupList;
   public ArrayList<myGoal> goalList;
   public ArrayList<myTask> taskList;
   public int curr_points;
   public String regTK;




    // constructor
    public User(String name,String email,String password,String reg_tk,int points) {
        this.regTK=reg_tk;
        this.name = name;
        this.email=email;
        this.password=password;
        this.groupList=new ArrayList<>();
        this.goalList=new ArrayList<>();
        this.taskList= new ArrayList<>();
        this.curr_points = points;
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
    public HashMap<String, Object> getArgs(){
        HashMap<String,Object> args= new HashMap<>();
        args.put("regTK",this.regTK);
        args.put("name",this.name);
        args.put("email",this.email);
        args.put("task-list",this.taskList);
        args.put("group-list",this.groupList);
        args.put("goal-list",this.goalList);
        args.put("curr-points",this.curr_points);
        return args;
    }

    @NonNull
    @Override
    public String toString() {
       return ("Full Name: "+this.name+", Mail: "+this.email+", Password : "+this.password+", Reg Token: "+this.regTK);
    }
}
