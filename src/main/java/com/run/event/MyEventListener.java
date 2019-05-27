package com.run.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 自定义监听类
 */
@Component
public class MyEventListener implements ApplicationListener<MyEvent> {
    private static Logger logger = LoggerFactory.getLogger(MyEventListener.class);

    @Override
    public void onApplicationEvent(MyEvent myEvent) {
        String msg = myEvent.getMsg();
        logger.info("received msg is :" + msg);
    }
}
