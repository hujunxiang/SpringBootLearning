package com.run.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件类
 */
@Data
public class MyEvent extends ApplicationEvent {

    private static final long serialVersion = -1L;

    private String msg;

    public MyEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }
}
