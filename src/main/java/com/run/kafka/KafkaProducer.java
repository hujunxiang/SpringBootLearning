package com.run.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaProducer {
    private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.consumer.group-id}")
    public String topicName;

    /**
     * 向kafka发送消息
     * @param obj
     */
    public void sendMsg(Object obj) {
        logger.info("准备发送消息为：{}", obj.toString());
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, obj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                logger.info(topicName + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.info(topicName + " - 生产者 发送消息失败：" + throwable.getMessage());
            }
        });
    }
}
