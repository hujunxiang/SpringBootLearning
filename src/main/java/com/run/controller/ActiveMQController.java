package com.run.controller;

import com.run.activemq.ActiveMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mq")
public class ActiveMQController {
    private static Logger logger = LoggerFactory.getLogger(ActiveMQController.class);

    @Autowired
    private ActiveMQProducer producer;

    @RequestMapping("sendQueueMsg/{num}")
    public String sendQueueMsg(@PathVariable int num) {
        for (int i = 0; i < num; i++) {
            producer.sendMessage(true,"jack count num is :" + i);
        }
        return "send queue message to activemq success";
    }

    @RequestMapping("sendTopicMsg/{num}")
    public String sendTopicMsg(@PathVariable int num) {
        for (int i = 0; i < num; i++) {
            producer.sendMessage(false,"jack count num is :" + i);
        }
        return "send topic message to activemq success";
    }
}
