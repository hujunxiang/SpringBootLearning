package com.run.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("ssc")
public class SpringSessionController {
    private static Logger logger = LoggerFactory.getLogger(SpringSessionController.class);

    @RequestMapping("setSession")
    public Map<String, Object> setSession(HttpServletRequest request) {
        request.getSession().setAttribute("mySession", "testSessionValue");
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", request.getSession().getId());
        result.put("session", request.getSession().toString());
        return result;
    }

    @RequestMapping("getSession")
    public String getSession(HttpServletRequest request) {
        return request.getSession().getAttribute("mySession").toString();
    }
}
