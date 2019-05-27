package com.run.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 自定义发布事件类
 */
@Component
public class MyEventPublisher {
    @Autowired
    private ApplicationContext context;

    public void publish(String msg){
        context.publishEvent(new MyEvent(this,msg));
    }
}
