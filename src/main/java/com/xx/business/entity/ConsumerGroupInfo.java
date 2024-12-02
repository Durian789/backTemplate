package com.xx.business.entity;

import java.util.Date;

public class ConsumerGroupInfo {

    private String topicName;
    private String groupName;
    private Integer lagCount;
    private Integer topicCount;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getLagCount() {
        return lagCount;
    }

    public void setLagCount(Integer lagCount) {
        this.lagCount = lagCount;
    }

    public Integer getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(Integer topicCount) {
        this.topicCount = topicCount;
    }
}
