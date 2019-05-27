package com.run.controller;

import com.run.event.MyEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("event")
public class MyEventController {
    @Autowired
    private MyEventPublisher publisher;

    @RequestMapping("publishMsg/{msg}")
    public void publishMsg(@PathVariable String msg) {
        publisher.publish(msg);
    }
}
