package com.springboot.learning.kafka.producer;

import javax.annotation.Resource;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.springboot.learning.entity.FamilyMember;

/**
 * @author forever_hgz
 */
@Service
public class KafkaProducerService {
    private static final String TOPIC_NAME = "test";

    // @Resource
    // private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private KafkaTemplate<String, FamilyMember> kafkaTemplate;

    // public void sendMessage(String message) {
    // kafkaTemplate.send(TOPIC_NAME, message);
    // }

    public void sendMessage(FamilyMember message) {
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
