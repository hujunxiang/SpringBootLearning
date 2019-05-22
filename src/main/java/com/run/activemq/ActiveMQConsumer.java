package com.run.activemq;

import com.run.aop.OptType;
import com.run.dao.SystemLogDao;
import com.run.model.SystemLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import java.util.Date;

/**
 * activemq消费者
 */
@Component
public class ActiveMQConsumer {
    private static Logger logger = LoggerFactory.getLogger(ActiveMQConsumer.class);

    @Autowired
    private SystemLogDao systemLogDao;

    /**
     * 接收queue里的消息并持久化到mysql数据库中
     * @param text
     */
    @JmsListener(destination = "myQueue")
    public void receiveQueueMsg(String text) {
        logger.info("收到Queue的报文为:" + text);
        SystemLog log = new SystemLog();
        log.setDescription(text);
        log.setCreateDate(new Date());
        log.setParams(text);
        log.setMethodUrl("myQueue");
        log.setOptType(OptType.SAVE);
        systemLogDao.save(log);
    }

    /**
     * JmsListener注解默认只接收queue消息,如果要接收topic消息,需要设置containerFactory
     *
     * @param factory
     * @return
     */
    @Bean
    public JmsListenerContainerFactory<?> topicContainerFactory(ConnectionFactory factory) {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);
        containerFactory.setConnectionFactory(factory);
        return containerFactory;
    }

    /**
     * 接收topic里的消息并持久化到mysql数据库中
     * @param text
     */
    @JmsListener(destination = "myTopic", containerFactory = "topicContainerFactory")
    public void receiveTopicMsg(String text) {
        logger.info("收到Topic的报文为:" + text);
        SystemLog log = new SystemLog();
        log.setDescription(text);
        log.setCreateDate(new Date());
        log.setParams(text);
        log.setMethodUrl("myTopic");
        log.setOptType(OptType.SAVE);
        systemLogDao.save(log);
    }
}
