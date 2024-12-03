package com.xx.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("kafka_group_topic")
public class KafkaGroupTopic {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("topic_name")
    private String topicName;

    @TableField("group_name")
    private String groupName;

    @TableField("lag_count")
    private Integer lagCount;

    @TableField("topic_count")
    private Integer topicCount;

    @TableField("lag_ratio")
    private Double lagRatio;

    @TableField("send_alarm_flag")
    private Boolean sendAlarmFlag;

    @TableField("create_time")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLagRatio() {
        return lagRatio;
    }

    public void setLagRatio(Double lagRatio) {
        this.lagRatio = lagRatio;
    }

    public Boolean getSendAlarmFlag() {
        return sendAlarmFlag;
    }

    public void setSendAlarmFlag(Boolean sendAlarmFlag) {
        this.sendAlarmFlag = sendAlarmFlag;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
