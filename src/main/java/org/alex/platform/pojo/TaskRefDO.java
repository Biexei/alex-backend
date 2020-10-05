package org.alex.platform.pojo;

import java.util.ArrayList;

public class TaskRefDO extends TaskDO{
    private ArrayList<Integer> emailId;

    public ArrayList<Integer> getEmailId() {
        return emailId;
    }

    public void setEmailId(ArrayList<Integer> emailId) {
        this.emailId = emailId;
    }
}
