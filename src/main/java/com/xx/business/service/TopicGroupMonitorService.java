package com.xx.business.service;

import com.xx.business.entity.ConsumerGroupInfo;

import java.util.List;
import java.util.Map;

public interface TopicGroupMonitorService {
    Map<String, Integer> allTopicCount(String topicName);

    List<ConsumerGroupInfo> consumerGroupLagCount();
}
