package com.run.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * activemq生产者
 */
@Component
public class ActiveMQProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${activemq.queue.name}")
    private String queueName;

    @Value("${activemq.topic.name}")
    private String topicName;

    /**
     * 发送消息
     *
     * @param isUseQueue 发生消息类型，true时使用queue，false时使用topic
     * @param message    待发送的消息
     */
    public void sendMessage(boolean isUseQueue, final String message) {
        Destination destination = null;
        if (isUseQueue) {
            destination = new ActiveMQQueue(queueName);
        } else {
            destination = new ActiveMQTopic(topicName);
        }
        jmsTemplate.setExplicitQosEnabled(true);//默认false，是否开启是否开启 deliveryMode, priority, timeToLive的配置
        jmsTemplate.setDeliveryMode(1);//设置是否持久化，1非持久化，2,持久化，默认2
        jmsTemplate.setTimeToLive(5000);//消息过期时间,单位为毫秒
        jmsTemplate.convertAndSend(destination, message);
    }
}
