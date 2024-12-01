package com.xx.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.business.entity.TopicGroupConfig;
import com.xx.business.mapper.TopicGroupConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TopicGroupConfigServiceImpl implements TopicGroupConfigService {

    @Autowired
    private TopicGroupConfigMapper topicGroupConfigMapper;

    //查询topic配置
    @Override
    public List<TopicGroupConfig> getConfig() {
        QueryWrapper<TopicGroupConfig> queryWrapper = new QueryWrapper<>();
//        设定一个查询条件 flag true
        queryWrapper.eq("flag", true);
        List<TopicGroupConfig> configList = topicGroupConfigMapper.selectList(queryWrapper);
        log.info("configList:{},{}", configList, "111");
        return configList;
    }
}
