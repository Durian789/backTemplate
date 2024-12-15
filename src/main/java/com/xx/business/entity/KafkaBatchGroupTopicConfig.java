package com.xx.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * CREATE TABLE `kafka_batch_group_topic_config` (
 * `ID` int NOT NULL AUTO_INCREMENT,
 * `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
 * `topic_rule` text,
 * `flag` tinyint(1) NOT NULL DEFAULT '1',
 * PRIMARY KEY (`ID`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
@TableName("kafka_batch_group_topic_config")
public class KafkaBatchGroupTopicConfig {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("group_name")
    private String groupName;

    /**
     * 非必填
     * 如果为空， 则对group下面所有TOPIC 进行监控
     * 如果配置，多个topic_rule用逗号隔开
     * - 如果包含 * 则为模糊匹配，*的代表任意字符
     * - 其他则是完全相等
     */
    @TableField("topic_rule")
    private String topicRule;

    @TableField("flag")
    private Boolean flag;

    @TableField(exist = false)
    private Set<String> equalsTopics;


    @TableField(exist = false)
    private Set<Pattern> fuzzyMatchTopics;

    public boolean checkTopic(String topic) {
        if (null != topic && !topic.isEmpty()) {
            if ((equalsTopics.isEmpty() && fuzzyMatchTopics.isEmpty())
                    || (!equalsTopics.isEmpty() && equalsTopics.contains(topic))) {
                return true;
            }
            if (!fuzzyMatchTopics.isEmpty()) {
                for (Pattern fuzzyMatchTopic : fuzzyMatchTopics) {
                    if (fuzzyMatchTopic.matcher(topic).find()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Set<String> getEqualsTopics() {
        return equalsTopics;
    }

    public void setEqualsTopics(Set<String> equalsTopics) {
        this.equalsTopics = equalsTopics;
    }

    public Set<Pattern> getFuzzyMatchTopics() {
        return fuzzyMatchTopics;
    }

    public void setFuzzyMatchTopics(Set<Pattern> fuzzyMatchTopics) {
        this.fuzzyMatchTopics = fuzzyMatchTopics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopicRule() {
        return topicRule;
    }

    public void setTopicRule(String topicRule) {
        this.topicRule = topicRule;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
