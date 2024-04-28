package com.springboot.learning.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.springboot.learning.entity.FamilyMember;
import com.springboot.learning.kafka.producer.KafkaProducerService;

import io.swagger.annotations.Api;

/**
 * @author forever_hgz
 */
@RestController
@Api(value = "/mq", tags = "消息队列测试用接口")
public class MessageQueueController {
    @Resource
    private KafkaProducerService kafkaProducerService;

    // @GetMapping("/kafkaMessageQueue/{message}")
    // public String kafkaMessageQueue(@PathVariable(name = "message") String message) {
    // kafkaProducerService.sendMessage(message);
    // return message;
    // }

    @GetMapping("/sendKafkaMessageQueueObjectMessage")
    public String sendKafkaMessageQueueObjectMessage(FamilyMember message) {
        kafkaProducerService.sendMessage(message);
        return JSON.toJSONString(message);
    }
}
