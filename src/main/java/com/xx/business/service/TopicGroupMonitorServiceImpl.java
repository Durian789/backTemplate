package com.xx.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.business.entity.KafkaGroupConfig;
import com.xx.business.entity.KafkaGroupTopic;
import com.xx.business.entity.KafkaTopicConfig;
import com.xx.business.mapper.KafkaGroupConfigMapper;
import com.xx.business.mapper.KafkaGroupTopicMapper;
import com.xx.business.mapper.KafkaTopicConfigMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class TopicGroupMonitorServiceImpl implements TopicGroupMonitorService {

    @Autowired
    private KafkaGroupTopicMapper kafkaGroupTopicMapper;
    @Autowired
    private KafkaGroupConfigMapper kafkaGroupConfigMapper;
    @Autowired
    private KafkaTopicConfigMapper kafkaTopicConfigMapper;

    @Override
    public void kafkaGroupTopic() {
        //开始时间
        LocalTime startTime = LocalTime.of(8, 0, 0);
        //结束时间
        LocalTime endTime = LocalTime.of(23, 59, 59, 999);
        //当前时间
        LocalTime now = LocalTime.now();
        //检查时间 是否在 08:00:00 - 23:59:59
        if (!(now.isAfter(startTime) && now.isBefore(endTime))) {
            log.info("kafkaGroupTopic 当前时间不合符计算逻辑：{}", now);
            return;
        }
        //去数据库 获取 group和topic的配置信息
        Set<String> allTopicConfig = getAllTopicConfig();
        Set<String> allGroupConfig = getAllGroupConfig();

        //写死配置
        //lag/topicCount 的阈值
        Double ratio = 0.5D;
        //绝对条目数
        int maxCount = 10000;
        /* --------------------------- 以上都是配置 ----------------------------*/

        //1. 获取 所有消费组 和 topic的关系数据
        String content = getKafkaGroupTopicData();
        //将结果转成JSON
        JSONObject jsonObject = JSONObject.parseObject(content);
        //获取 consumerGroups json数组
        JSONArray consumerGroups = jsonObject.getJSONArray("consumerGroups");
        //如果不为空 就进行遍历
        if (consumerGroups != null && !consumerGroups.isEmpty()) {
            //开始遍历
            for (int i = 0; i < consumerGroups.size(); i++) {
                JSONObject data = consumerGroups.getJSONObject(i);
                //获取到 groupId
                String groupId = data.getString("groupId");
                //获取每个 消费组 对应的Topic数组
                JSONArray topicOffsets = data.getJSONArray("topicOffsets");
                //如果 topicOffsets 数组不为空
                if (topicOffsets != null && !topicOffsets.isEmpty()) {
                    //遍历 topicOffsets
                    for (int j = 0; j < topicOffsets.size(); j++) {
                        JSONObject topic = topicOffsets.getJSONObject(j);
                        //如果topic数据不为空
                        if (null != topic) {
                            try {
                                //创建KafkaGroupTopic对象 用于记录 group 和 topic的关系对象 并写入数据库
                                KafkaGroupTopic consumerGroupInfo = new KafkaGroupTopic();
                                //赋值 groupName
                                consumerGroupInfo.setGroupName(groupId);
                                //获取topic名称
                                String topicName = topic.getString("topic");
                                consumerGroupInfo.setTopicName(topicName);
                                //TODO 根据topicName 获取 topic总量
                                Integer topicCount = getKafkaTopicCountData(topicName);
                                consumerGroupInfo.setTopicCount(topicCount);
                                //Lag总量
                                Integer summedLag = topic.getInteger("summedLag");
                                consumerGroupInfo.setLagCount(summedLag);
                                //数据的计算时间
                                consumerGroupInfo.setCreateTime(new Date());
                                //判断数据 是否配置 并且大于阈值
                                checkAndSendAlarm(consumerGroupInfo, allTopicConfig, allGroupConfig, ratio);
                                //插入数据表
                                kafkaGroupTopicMapper.insert(consumerGroupInfo);
                            } catch (Exception e) {
                                log.error("INVALID DATA:", e);
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * 校验数据
     * 是否为 配置中的 group 和 topic
     * 如果是 则 判断 lag/count的值 是否大于 阈值
     * 如果大于 则 发送预警
     */
    private void checkAndSendAlarm(KafkaGroupTopic consumerGroupInfo,
                                   Set<String> allTopicConfig,
                                   Set<String> allGroupConfig,
                                   Double ratio) {
        try {
            //先校验consumerGroupInfo 数据是否正常
            if (null != consumerGroupInfo.getGroupName()
                    && null != consumerGroupInfo.getTopicName()
                    && null != consumerGroupInfo.getLagCount()
                    && 0 < consumerGroupInfo.getLagCount()
                    && null != consumerGroupInfo.getTopicCount()
                    && 0 < consumerGroupInfo.getTopicCount()
                    //校验group是否配置
                    && allGroupConfig.contains(consumerGroupInfo.getGroupName())
                    //检查topic是否配置
                    && allTopicConfig.contains(consumerGroupInfo.getTopicName())) {
                //进行计算 lag/topicCount
                BigDecimal bdA = new BigDecimal(consumerGroupInfo.getLagCount());
                BigDecimal bdB = new BigDecimal(consumerGroupInfo.getTopicCount());
                //保留三位小数
                Double lagRatio = bdA.divide(bdB, 3, RoundingMode.HALF_UP).doubleValue();
                //结果记录到表里面
                consumerGroupInfo.setLagRatio(lagRatio);
                //大于阈值 发送预警
                if (lagRatio > ratio) {
                    //TODO 发送预警还没写

                    consumerGroupInfo.setSendAlarmFlag(true);
                } else {
                    consumerGroupInfo.setSendAlarmFlag(false);
                }
            }
        } catch (Exception e) {
            log.error("checkAndSendAlarm 发送预警异常:", e);
        }
    }

    /**
     * 查询 group的配置信息
     */
    private Set<String> getAllGroupConfig() {
        QueryWrapper<KafkaGroupConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", true);
        List<KafkaGroupConfig> kafkaGroupConfigs = kafkaGroupConfigMapper.selectList(queryWrapper);
        //groupId 的去重集合
        Set<String> groupIds = new HashSet<>();
        if (null != kafkaGroupConfigs && !kafkaGroupConfigs.isEmpty()) {
            for (KafkaGroupConfig kafkaGroupConfig : kafkaGroupConfigs) {
                //循环遍历后将groupName放入到 去重集合中
                groupIds.add(kafkaGroupConfig.getGroupName());
            }
        }
        //最后获取到 kafka_group_config 这张表里面 flag为true的所有 groupName
        return groupIds;
    }

    /**
     * 查询 topic的配置信息
     */
    private Set<String> getAllTopicConfig() {
        QueryWrapper<KafkaTopicConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", true);
        List<KafkaTopicConfig> kafkaTopicConfigs = kafkaTopicConfigMapper.selectList(queryWrapper);
        //topicName 的去重集合
        Set<String> topicNames = new HashSet<>();
        if (null != kafkaTopicConfigs && !kafkaTopicConfigs.isEmpty()) {
            for (KafkaTopicConfig kafkaTopicConfig : kafkaTopicConfigs) {
                //循环遍历后将 topicName 放入到 去重集合中
                topicNames.add(kafkaTopicConfig.getTopicName());
            }
        }
        //最后获取到 kafka_topic_config 这张表里面 flag为true的所有 topicName
        return topicNames;
    }

    /**
     * 调用本地模拟接口 - group和topic的关系
     * TODO 本地测试完成之后 需要修改成真实接口
     */
    public String getKafkaGroupTopicData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //TODO 需要换成真实接口
                .url("http://127.0.0.1:9998/mock/consumerGroupData")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                log.error("getKafkaGroupTopicData 调用接口失败，状态码: {}", response.code());
                return null;
            }
        } catch (IOException e) {
            log.error("getKafkaGroupTopicData 调用接口出现IOException异常", e);
            return null;
        }
    }

    /**
     * 调用本地模拟接口 - topic的总量
     * TODO 本地测试完成之后 需要修改成真实接口
     */
    public Integer getKafkaTopicCountData(String topicName) {
        OkHttpClient client = new OkHttpClient();
        //TODO 需要换成真实接口
        String url = String.format("http://127.0.0.1:9998/mock/topicData?topicName=%s", topicName);
        Request request = new Request.Builder()
                .url(url)
                .build();
        //定义了 topicCount
        int topicCount = 0;
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String string = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(string);
                if (null != jsonObject) {
                    //拿到 这个 json中的数组对象
                    JSONArray partitions = jsonObject.getJSONArray("partitions");
                    //判断这个topic的分片是否存在 并且不为空
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
                        return topicCount;
                    }
                }
            } else {
                log.error("getKafkaTopicCountData 调用接口失败，状态码: {}", response.code());
            }
        } catch (IOException e) {
            log.error("getKafkaTopicCountData 调用接口出现IOException异常", e);
        }
        return topicCount;
    }

}