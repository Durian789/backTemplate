package com.xx.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xx.business.entity.ConsumerGroupInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class TopicGroupMonitorServiceImpl implements TopicGroupMonitorService {


    private static String getStringByFilePath(String filePath) {
        try {
            // 使用java.nio.file.Files的readAllBytes方法读取文件的所有字节
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            // 将字节转换为字符串
            return new String(bytes);
        } catch (IOException e) {
            log.error("getStringByFilePath ERROR:", e);
        }
        return "";
    }

    @Override
    public Map<String, Integer> allTopicCount(String topicName) {
        Map<String, Integer> map = new HashMap<>();
        //TODO 这一段 可能换成调用接口 需要传入topicName当作参数
        String content = getStringByFilePath("C:\\Users\\小小\\Desktop\\topicCount.txt");
        //根据返回值 获取 topic的总量
        JSONObject jsonObject = JSONObject.parseObject(content);
        //拿到 这个 json中的数组对象
        JSONArray partitions = jsonObject.getJSONArray("partitions");
        //定义了 topicCount
        int topicCount = 0;
        //判断这个topic的分组是否存在
        if (partitions != null && !partitions.isEmpty()) {
            //对topic分片进行遍历
            for (int i = 0; i < partitions.size(); i++) {
                try {
                    JSONObject data = partitions.getJSONObject(i);
                    Integer waterMarkHigh = data.getInteger("waterMarkHigh");
                    Integer waterMarkLow = data.getInteger("waterMarkLow");
                    //每个分片的最大值 - 最小值 进行累加 获得topic总数
                    topicCount = topicCount + (waterMarkHigh - waterMarkLow);
                } catch (Exception e) {
                    log.error("INVALID DATA:", e);
                }
            }
            map.put(jsonObject.getString("topicName"), topicCount);
        }
        return map;
    }

    @Override
    public List<ConsumerGroupInfo> consumerGroupLagCount() {
        List<ConsumerGroupInfo> allGroupInfos = new ArrayList<>();
        //TODO 也可能换成接口 获取消费组 的数据
        String content = getStringByFilePath("C:\\Users\\小小\\Desktop\\consumerGroupInfo.txt");

        JSONObject jsonObject = JSONObject.parseObject(content);

        JSONArray consumerGroups = jsonObject.getJSONArray("consumerGroups");

        if (consumerGroups != null && !consumerGroups.isEmpty()) {
            for (int i = 0; i < consumerGroups.size(); i++) {

                JSONObject data = consumerGroups.getJSONObject(i);
                String groupId = data.getString("groupId");
                JSONArray topicOffsets = data.getJSONArray("topicOffsets");
                if (topicOffsets != null && !topicOffsets.isEmpty()) {
                    for (int j = 0; j < topicOffsets.size(); j++) {
                        JSONObject topic = topicOffsets.getJSONObject(j);
                        try {
                            ConsumerGroupInfo consumerGroupInfo = new ConsumerGroupInfo();
                            String topicName = topic.getString("topic");
                            //获取 topic总量
                            Map<String, Integer> topicCountMap = allTopicCount(topicName);

                            Integer summedLag = topic.getInteger("summedLag");
                            consumerGroupInfo.setGroupName(groupId);
                            consumerGroupInfo.setTopicName(topicName);
                            //Lag总量
                            consumerGroupInfo.setLagCount(summedLag);
                            Integer topicCount = topicCountMap.getOrDefault(topicName, 0);
                            consumerGroupInfo.setTopicCount(topicCount);
                            consumerGroupInfo.setCreateTime(new Date());

                            //TODO 改成插入数据库
                            allGroupInfos.add(consumerGroupInfo);
                        } catch (Exception e) {
                            log.error("INVALID DATA:", e);
                        }
                    }
                }
            }
        }
        return allGroupInfos;
    }
}
