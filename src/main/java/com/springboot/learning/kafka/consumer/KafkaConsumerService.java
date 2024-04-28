package com.springboot.learning.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.springboot.learning.entity.FamilyMember;

import lombok.extern.slf4j.Slf4j;

/**
 * @author forever_hgz
 */
@Slf4j
@Service
public class KafkaConsumerService {
    private static final String TOPIC_NAME = "test";

    // @KafkaListener(topics = TOPIC_NAME, groupId = "myGroupId")
    // public void listen(String message) {
    // log.info("Received message 1: {}", message);
    // // 处理接收到的消息
    // }

    // @KafkaListener(topics = TOPIC_NAME, groupId = "myGroupId")
    @KafkaListener(topics = TOPIC_NAME, groupId = "myGroupId",
        properties = {"spring.json.value.default.type=com.springboot.learning.entity.FamilyMember"})
    public void listen(FamilyMember message) {
        log.info("Received message 2: {}", JSON.toJSONString(message));
        // 处理接收到的消息
    }
}
