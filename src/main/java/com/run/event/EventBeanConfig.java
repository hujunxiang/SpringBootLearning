package com.run.event;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描com.run.event包下的类，并注入
 */
@Configuration
@ComponentScan("com.run.event")
public class EventBeanConfig {
}
