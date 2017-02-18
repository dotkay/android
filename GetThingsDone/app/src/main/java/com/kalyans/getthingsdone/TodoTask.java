package com.kalyans.getthingsdone;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kalyans on 2/11/2017.
 */


public class TodoTask {
    enum Priority {HIGH, MEDIUM, LOW};
    public String taskText;
    // public Date taskDeadline;
    public Priority taskPriority;

    public TodoTask(String taskName, Priority taskPriority) {
        this.taskText = taskName;
        // this.taskDeadline = completeBy;
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
}
