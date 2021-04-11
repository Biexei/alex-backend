package org.alex.platform.pojo;

import java.util.List;

public class FeedbackDTO extends FeedbackDO{
    private List<Integer> statusList;

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }
}
