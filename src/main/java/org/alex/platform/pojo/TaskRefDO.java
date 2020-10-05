package org.alex.platform.pojo;

import java.util.ArrayList;

public class TaskRefDO extends TaskDO{
    private ArrayList<String> emailList;

    public ArrayList<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<String> emailList) {
        this.emailList = emailList;
    }
}
