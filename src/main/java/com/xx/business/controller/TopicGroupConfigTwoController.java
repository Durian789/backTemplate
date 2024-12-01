package com.xx.business.controller;

import com.xx.business.entity.TopicGroupConfigTwo;
import com.xx.business.service.TopicGroupConfigTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class TopicGroupConfigTwoController {

    @Autowired
    private TopicGroupConfigTwoService topicGroupConfigTwoService;

    @RequestMapping("/topic")
    public List<TopicGroupConfigTwo> getConfig() {
        return topicGroupConfigTwoService.getConfig();
    }
}
