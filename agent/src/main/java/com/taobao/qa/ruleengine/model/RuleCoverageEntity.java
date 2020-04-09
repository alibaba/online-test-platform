package com.taobao.qa.ruleengine.model;

import java.util.Date;

public class RuleCoverageEntity {

    public int taskid;
    public int realtime_cov;
    public int static_total_cov;
    public int static_cansmoke_cov;
    public String timestamp;

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getRealtime_cov() {
        return realtime_cov;
    }

    public void setRealtime_cov(int realtime_cov) {
        this.realtime_cov = realtime_cov;
    }

    public int getStatic_total_cov() {
        return static_total_cov;
    }

    public void setStatic_total_cov(int static_total_cov) {
        this.static_total_cov = static_total_cov;
    }

    public int getStatic_cansmoke_cov() {
        return static_cansmoke_cov;
    }

    public void setStatic_cansmoke_cov(int static_cansmoke_cov) {
        this.static_cansmoke_cov = static_cansmoke_cov;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
