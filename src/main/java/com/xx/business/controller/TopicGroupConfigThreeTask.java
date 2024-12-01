package com.xx.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.business.entity.TopicGroupConfigThree;
import com.xx.business.mapper.TopicGroupConfigThreeMapper;
import com.xx.client.YzajxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class TopicGroupConfigThreeTask {

    @Autowired
    private TopicGroupConfigThreeMapper topicGroupConfigThreeMapper;

    @Autowired
    private YzajxClient yzajxClient;

    @RequestMapping("/three/topic")
    public List<TopicGroupConfigThree> getConfig() {
        QueryWrapper<TopicGroupConfigThree> req = new QueryWrapper<>();
        req.likeRight("`group`","AA");
        List<TopicGroupConfigThree> res = topicGroupConfigThreeMapper.selectList(req);
        return res;
    }

    @PostMapping("/three/insertTopic")
    public TopicGroupConfigThree insertConfig(@RequestBody JSONObject jsonObject) {

        TopicGroupConfigThree topicGroupConfigThree = new TopicGroupConfigThree();
        topicGroupConfigThree.setGroup(jsonObject.getString("group"));
        topicGroupConfigThree.setTopic(jsonObject.getString("topic"));
        topicGroupConfigThree.setFlag(jsonObject.getBoolean("flag"));
        //插入数据
        topicGroupConfigThreeMapper.insert(topicGroupConfigThree);
        return topicGroupConfigThree;
    }

    @RequestMapping("/three/jzInfo")
    public String jzInfo() {
        return yzajxClient.getAjxInfo();
    }

    @RequestMapping("/three/postArticles")
    public String postArticles() {
        return yzajxClient.postArticles();
    }

}
