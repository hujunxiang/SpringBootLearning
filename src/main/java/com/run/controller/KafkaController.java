package com.run.controller;

import com.run.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("kafka")
public class KafkaController {
    private static Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaProducer producer;

    /**
     * 向kafka发送消息
     * @return
     */
    @RequestMapping("sendMsg")
    public String sendMsg() {
        Random r = new Random();
        int num = r.nextInt(100) + 1;
        for (int i = 0; i < num; i++) {
            producer.sendMsg("hello kafka , i am producer " + i);
        }
        return "producer get total num is :" + num;
    }
}
