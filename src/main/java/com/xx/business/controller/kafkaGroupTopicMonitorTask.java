package com.xx.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.business.entity.KafkaGroupTopic;
import com.xx.business.mapper.KafkaGroupTopicMapper;
import com.xx.business.service.TopicGroupMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
//@JobHandler(value = "kafkaGroupTopicMonitorTask")
@Component
@Slf4j
public class kafkaGroupTopicMonitorTask {

    @Autowired
    private KafkaGroupTopicMapper kafkaGroupTopicMapper;

    @Autowired
    private TopicGroupMonitorService topicGroupMonitorService;


    //获取数据库中的group
    @RequestMapping("/test/topic")
    public List<KafkaGroupTopic> getConfig() {
        QueryWrapper<KafkaGroupTopic> req = new QueryWrapper<>();
        req.likeRight("`group_name`", "sc");
        List<KafkaGroupTopic> res = kafkaGroupTopicMapper.selectList(req);
        return res;
    }

    @RequestMapping("/test/consumerGroupLagCount")
    public String consumerGrouplagCount() {
        topicGroupMonitorService.kafkaGroupTopic("0.11");
        return "OK";
    }

}