package com.run.controller;

import com.run.model.Account;
import com.run.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller层
 */
@RestController
@RequestMapping("test")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 获取所有account信息
     *
     * @return
     */
    @RequestMapping("getAllAccount")
    public List<Account> getAllAccount() {
        return accountService.getAllAccount();
    }

    /**
     * 分页查询
     *
     * @param pageSize 每页大小
     * @param pageNum  第几页
     * @return
     */
    @RequestMapping("findAccountByConditions")
    public Page<Account> findAccountByConditions(@RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                 @RequestParam(value = "pageNum", defaultValue = "0", required = true) int pageNum) {
        return accountService.findAccountByConditions(pageNum, pageSize);
    }

    /**
     * 初始化account信息
     *
     * @return
     */
    @RequestMapping(value = "initAccount/{index}", method = RequestMethod.GET)
    public String initAccount(@PathVariable int index) {
        List<Integer> idList = new LinkedList<>();
        for (int i = index; i < index + 100; i++) {
            Account account = new Account();
            account.setName("张三" + i);
            account.setEmail("90598269" + i + "@qq.com");
            accountService.saveAccount(account);
            idList.add(account.getId());
        }
        return idList.toString();
    }

    @RequestMapping("findAccountById")
    public String findAccountById(int id) {
        Account account = accountService.findAccountById(id);
        if (StringUtils.isEmpty(account)) {
            return "can not find record by id = " + id;
        } else {
            return account.getId().toString();
        }
    }

    @RequestMapping("deleteAccountById")
    public String deleteAccountById(int id) {
        String flag = "success";
        try {
            accountService.deleteAccountById(id);
        } catch (Exception e) {
            flag = "fail";
            e.printStackTrace();
        }
        return flag;
    }
}
