package com.kalyans.getthingsdone;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static com.kalyans.getthingsdone.TodoTask.Priority.HIGH;
import static com.kalyans.getthingsdone.TodoTask.Priority.LOW;
import static com.kalyans.getthingsdone.TodoTask.Priority.MEDIUM;

/**
 * Created by kalyans on 2/11/2017.
 */


public class TodoTask {
    enum Priority {HIGH, MEDIUM, LOW};

    public int taskID;
    public String taskText;
    // public Date taskDeadline;
    public Priority taskPriority;

    public TodoTask() {

    }

    public TodoTask(String taskName, Priority taskPriority) {
        this.taskText = taskName;
        // this.taskDeadline = completeBy;
        this.taskPriority = taskPriority;
    }

    public TodoTask(String taskName, String taskPriorityString) {
        this.taskText = taskName;
        this.taskPriority = getPriority(taskPriorityString);
    }

    // getters and setters
    public int getTaskID() {
        return this.taskID;
    }

    public String getTaskName() {
        return this.taskText;
    }

    public String getTaskPriority() {
        return getStringPriority(this.taskPriority);
    }

    public void setTaskID(int id) {
        this.taskID = id;
    }

    public void setTaskText(String taskName) {
        this.taskText = taskName;
    }

    public void setTaskPriority(Priority taskPriority) {
        this.taskPriority = taskPriority;
    }


    public String getStringPriority(Priority taskPriority) {
        String strPriority = null;
        switch(taskPriority) {
            case HIGH:
                strPriority = "HIGH";
                break;
            case MEDIUM:
                strPriority = "MEDIUM";
                break;
            case LOW:
                strPriority = "LOW";
                break;
        }
        return strPriority;
    }

    public Priority getPriority(String pstr) {
        Priority taskPriority = null;
        switch(pstr) {
            case "HIGH":
                taskPriority = HIGH;
                break;
            case "MEDIUM":
                taskPriority = MEDIUM;
                break;
            case "LOW":
                taskPriority = LOW;
                break;
        }
        return taskPriority;
    }
}
