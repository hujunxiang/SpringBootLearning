package com.run.controller;

import com.github.pagehelper.PageHelper;
import com.run.aop.AopLog;
import com.run.aop.OptType;
import com.run.model.Account;
import com.run.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

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
    @AopLog(moduleName = "查询Account列表",description = "分页查询列表信息",operType = OptType.QUERY)
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
    public List<Account> initAccount(@PathVariable int index) {
        List<Account> accountList = new LinkedList<>();
        for (int i = index; i < index + 100; i++) {
            Account account = new Account();
            account.setName("张三" + i);
            account.setEmail("90598269" + i + "@qq.com");
            accountService.saveAccount(account);
            accountList.add(account);
        }
        return accountList;
    }

    @RequestMapping("findAccountById")
    public Account findAccountById(int id) {
        Account account = accountService.findAccountById(id);
        return null;
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

    /**
     * 使用mybatis查询单条记录
     *
     * @param id
     * @return
     */
    @RequestMapping("findAccountByIdUseMyBatis/{id}")
    public Account findAccountByIdUseMyBatis(@PathVariable int id) {
        return accountService.findAccountByIdUseMyBatis(id);
    }

    /**
     * 使用PageHelper进行分页查询
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping("findAccountList")
    public List<Account> findAccountList(int pageSize, int pageNum,Account account) {
        return accountService.findAccountList(pageSize, pageNum, account);
    }
}
