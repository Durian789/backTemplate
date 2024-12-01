package com.xx.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.dao.TopicConfig;
import com.xx.dao.TopicConfigMapper;
import com.xx.dao.User;
import com.xx.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/test")
@Slf4j
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TopicConfigMapper topicConfigMapper;

    @GetMapping("/get")
    public List<User> getTest() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "zs");

        List<User> users = userMapper.selectList(queryWrapper);
        return users;
    }

    @RequestMapping("/getConfig")
    public List<TopicConfig> getConfig(@RequestParam String s) {

        String[] configArray = StringUtils.delimitedListToStringArray(s, ",");
        if (configArray.length!= 3) {
            log.error("传入的参数格式不正确，需要包含两个设定值，以逗号分隔");
            return null;
        }

        //两个设定值 比值阈值 绝对条目数阈值
        double ratioThreshold = Double.parseDouble(configArray[0]);
        long countThreshold = Long.parseLong(configArray[1]);
        log.info("");
        String groupName = configArray[2];

        //定义一个查询topic_config这张表的查询条件
        QueryWrapper<TopicConfig> queryWrapper = new QueryWrapper<>();
        //设定一个条件 group_name = 'group1'
        //queryWrapper.eq("group_name", groupName);
        //设定一个条件 group_name like '%group1%'
        queryWrapper.like("group_name", groupName);
        //查询
        List<TopicConfig> topicConfigs = topicConfigMapper.selectList(queryWrapper);
        return topicConfigs;
    }


}
