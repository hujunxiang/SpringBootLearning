package com.run.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * 从kafka接受消息
     * @param record
     * @param topic
     */
    @KafkaListener(topics = "myGroupId", groupId = "myGroupId")
    public void receiveMsg(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("myGroupId start receive msg ...");
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            logger.info("被myGroupId消费了：Topic:" + topic + ",Record:" + record + ",Message:" + msg);
        }
    }
}
