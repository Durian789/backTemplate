package com.xx.business.controller;

import com.xx.business.entity.TopicGroupConfig;
import com.xx.business.service.TopicGroupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicGroupConfigController {

    @Autowired
    private TopicGroupConfigService topicGroupConfigService;

    @RequestMapping("/getConfig")
    public List<TopicGroupConfig> getConfig() {
        return topicGroupConfigService.getConfig();
    }
}
