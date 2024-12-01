package com.xx.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.business.entity.TopicGroupConfigTwo;
import com.xx.business.mapper.TopicGroupConfigTwoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TopicGroupConfigTwoServiceImpl implements TopicGroupConfigTwoService {

    @Autowired
    private TopicGroupConfigTwoMapper topicGroupConfigTwoMapper;

    //查询topic配置
    public List<TopicGroupConfigTwo> getConfig() {
        QueryWrapper<TopicGroupConfigTwo> rearchList = new QueryWrapper<>();
//        设定一个查询条件
        rearchList.eq("flag", true);
        List<TopicGroupConfigTwo> dataList = topicGroupConfigTwoMapper.selectList(rearchList);
        return dataList;
    }
}
