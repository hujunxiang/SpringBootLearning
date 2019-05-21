package com.run.controller;

import com.run.model.Account;
import com.run.reids.RedisUtils;
import com.run.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("redis")
public class RedisController {
    private static Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private AccountService accountService;

    @RequestMapping("setInfo2Redis/{key}/{value}")
    public String setInfo2Redis(@PathVariable String key, @PathVariable String value) {
        redisUtils.set(key, value);
        return redisUtils.get(key) + ":" + value;
    }

    @RequestMapping("getInfoFromRedisByKey/{key}")
    public String getInfoFromRedisByKey(@PathVariable String key) {
        return "key:" + redisUtils.get(key);
    }

    @RequestMapping("delInfoFromRedisByKeys/{key}")
    public boolean delInfoFromRedisByKeys(@PathVariable String key) {
        return redisUtils.deleteByKeys(key);
    }

    @RequestMapping("getAccountListFormRedisOrDB/{name}/{email}")
    public List<Account> getAccountListFormRedisOrDB(@PathVariable String name, @PathVariable String email) {
        return accountService.getAccountListFormRedisOrDB(name, email);
    }

    @RequestMapping("getAccountListFormRedisOrDBByName")
    public List<Account> getAccountListFormRedisOrDBByName(String name) {
        return accountService.getAccountListFormRedisOrDBByName(name);
    }

    @RequestMapping("getAccountListFormRedisOrDBByEmail")
    public List<Account> getAccountListFormRedisOrDBByEmail(String email) {
        return accountService.getAccountListFormRedisOrDBByEmail(email);
    }
}
